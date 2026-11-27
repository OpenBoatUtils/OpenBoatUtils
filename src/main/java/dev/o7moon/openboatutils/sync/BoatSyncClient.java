package dev.o7moon.openboatutils.sync;

//? >= 26.1 {
/*import dev.o7moon.openboatutils.ISettingContext;
import dev.o7moon.openboatutils.OpenBoatUtils;
import io.github.openboatutils.protocol.channels.OBUSyncPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Client side of openboatutils:sync. Only does anything while the active context has boat sync on.
public final class BoatSyncClient {
    private BoatSyncClient() {}

    private static final long TRACK_TIMEOUT_NANOS = 10_000_000_000L;
    private static final long PING_TIMEOUT_NANOS = 5_000_000_000L;
    private static final int RTT_SAMPLES = 10;
    private static final double MAX_SERVER_DISTANCE = 128.0;

    // entity id -> track, client thread only
    private static final Map<Integer, RemoteBoatTrack> TRACKS = new HashMap<>();

    // The pong handler (network thread) and the probe thread are the only things off the client thread.
    // ping request time value -> send time (the same value, kept to expire lost pings)
    private static final Map<Long, Long> PENDING_PINGS = new ConcurrentHashMap<>();
    private static final long[] RTTS = new long[RTT_SAMPLES];
    private static int rttCount = 0;
    private static int rttNext = 0;
    private static volatile float halfRttMs = 0;

    private static volatile boolean probing = false;
    private static volatile long probingUpdatedNanos = 0;
    private static Thread probeThread;

    private static int pingTicks = 0;
    private static int sendTicks = 0;

    private static int getInterval() {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();
        return context == null ? 0 : context.getBoatSyncInterval();
    }

    public static boolean isEnabled() {
        return getInterval() > 0;
    }

    public static float getHalfRttMs() {
        return halfRttMs;
    }

    private static boolean canSend() {
        return ClientPlayNetworking.canSend(OpenBoatUtils.SYNC_CHANNEL.getId());
    }

    public static boolean isLocalDriver(AbstractBoat boat) {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null
                && boat.level().isClientSide()
                && boat.getControllingPassenger() == player
                && boat.isLocalInstanceAuthoritative();
    }

    // End of every client tick.
    public static void tick(Minecraft minecraft) {
        ClientPacketListener connection = minecraft.getConnection();

        if (connection == null || !isEnabled()) {
            probing = false;
            TRACKS.clear();
            return;
        }

        if (++pingTicks >= 20) {
            pingTicks = 0;
            sendPing(connection);
        }

        boolean driving = minecraft.player != null
                && minecraft.player.getVehicle() instanceof AbstractBoat boat
                && isLocalDriver(boat);

        probing = driving && canSend();
        probingUpdatedNanos = System.nanoTime();
        if (probing) ensureProbeThread();

        long now = System.nanoTime();
        TRACKS.values().removeIf(track -> now - track.getLastReceiveNanos() > TRACK_TIMEOUT_NANOS);
    }

    public static void clear() {
        TRACKS.clear();
        PENDING_PINGS.clear();
        synchronized (RTTS) {
            rttCount = 0;
            rttNext = 0;
        }
        halfRttMs = 0;
        probing = false;
        sendTicks = 0;
    }

    // RTT

    private static void sendPing(ClientPacketListener connection) {
        long now = System.nanoTime();
        PENDING_PINGS.values().removeIf(sent -> now - sent > PING_TIMEOUT_NANOS);
        PENDING_PINGS.put(now, now);
        connection.send(new ServerboundPingRequestPacket(now));
    }

    // Network thread. Returns true if the pong answered one of our pings (vanilla must not see it).
    public static boolean onPong(long time, long receiveNanos) {
        Long sent = PENDING_PINGS.remove(time);
        if (sent == null) return false;

        synchronized (RTTS) {
            RTTS[rttNext] = receiveNanos - sent;
            rttNext = (rttNext + 1) % RTT_SAMPLES;
            rttCount = Math.min(rttCount + 1, RTT_SAMPLES);

            long min = Long.MAX_VALUE;
            for (int i = 0; i < rttCount; i++) min = Math.min(min, RTTS[i]);
            halfRttMs = min / 2_000_000f;
        }

        return true;
    }

    // Sending

    // End of the driven boat's tick.
    public static void sendState(AbstractBoat boat, float yawRate, float yawAccel, float friction, float ax, float az) {
        int interval = Math.min(getInterval(), 4);
        if (interval <= 0 || !canSend()) return;

        // a state every interval ticks
        if (++sendTicks < interval) return;
        sendTicks = 0;

        Vec3 position = boat.position();
        Vec3 velocity = boat.getDeltaMovement();

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(OBUSyncPacket.STATE_LENGTH));
        buf.writeShort(0);
        buf.writeLong(System.nanoTime());
        buf.writeFloat(halfRttMs);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeFloat((float) velocity.x);
        buf.writeFloat((float) velocity.y);
        buf.writeFloat((float) velocity.z);
        buf.writeFloat(boat.getYRot());
        buf.writeFloat(yawRate);
        buf.writeFloat(yawAccel);
        buf.writeFloat(friction);
        buf.writeFloat(ax);
        buf.writeFloat(az);
        buf.writeByte(0);

        OpenBoatUtils.SYNC_CHANNEL.sendPacketC2S(buf);
    }

    private static void ensureProbeThread() {
        if (probeThread != null) return;

        probeThread = new Thread(BoatSyncClient::probeLoop, "OpenBoatUtils Boat Sync Probe");
        probeThread.setDaemon(true);
        probeThread.start();
    }

    // Probes go out about 8 times a second spread over the tick phase, see SyncAgeEstimator.probeTimeNanos.
    private static void probeLoop() {
        long start = System.nanoTime();
        long k = 0;

        while (true) {
            try {
                long wait = start + SyncAgeEstimator.probeTimeNanos(++k) - System.nanoTime();
                if (wait < 0) {
                    // fell behind (stall or suspend), restart the schedule from now
                    start = System.nanoTime();
                    k = 0;
                    continue;
                }
                Thread.sleep(wait / 1_000_000, (int) (wait % 1_000_000));

                if (!probing || System.nanoTime() - probingUpdatedNanos > 250_000_000L) continue;

                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(OBUSyncPacket.PROBE_LENGTH));
                buf.writeShort(1);
                buf.writeLong(System.nanoTime());
                buf.writeFloat(halfRttMs);

                OpenBoatUtils.SYNC_CHANNEL.sendPacketC2S(buf);
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                // disconnected between the check and the send, try again later
            }
        }
    }

    // Receiving

    // Client thread, from the Fabric receiver. Packets are dropped while sync is off.
    // receivedNanos is when the packet came off the network, the handler itself waits for a frame.
    public static void receive(ByteBuf data, long receivedNanos) {
        if (!isEnabled() || data.readableBytes() < 6) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        int entityId = data.getInt(data.readerIndex());
        if (!(level.getEntity(entityId) instanceof AbstractBoat boat)) return;

        onPacket(data, receivedNanos, boat);
    }

    // Client thread. data = int vehicle entity id + the sender's serverbound packet.
    private static void onPacket(ByteBuf data, long receiveNanos, AbstractBoat boat) {
        if (data.readableBytes() < 6) return;

        int entityId = data.readInt();
        short packetId = data.readShort();
        int remaining = data.readableBytes();

        if (packetId == 0 && remaining == OBUSyncPacket.STATE_LENGTH - 2) {
            long stamp = data.readLong();
            float senderHalfRtt = data.readFloat();

            SyncPredictor.State state = new SyncPredictor.State();
            state.x = data.readDouble();
            state.y = data.readDouble();
            state.z = data.readDouble();
            state.vx = data.readFloat();
            state.vy = data.readFloat();
            state.vz = data.readFloat();
            state.yaw = data.readFloat();
            state.yawRate = data.readFloat();
            state.yawAccel = data.readFloat();
            state.friction = data.readFloat();
            state.ax = data.readFloat();
            state.az = data.readFloat();
            data.readByte(); // flags, reserved

            if (!state.isValid()) return;

            // the boat's last server-sent position, a relayed state can not be far from it
            Vec3 base = boat.getPositionCodec().getBase();
            if (!state.isNear(base.x, base.y, base.z, MAX_SERVER_DISTANCE)) return;

            track(entityId).onState(stamp, receiveNanos, senderHalfRtt, state);
        } else if (packetId == 1 && remaining == OBUSyncPacket.PROBE_LENGTH - 2) {
            long stamp = data.readLong();
            float senderHalfRtt = data.readFloat();

            track(entityId).onProbe(stamp, receiveNanos, senderHalfRtt);
        }
    }

    private static RemoteBoatTrack track(int entityId) {
        return TRACKS.computeIfAbsent(entityId, unused -> new RemoteBoatTrack());
    }

    public static @Nullable RemoteBoatTrack getTrack(AbstractBoat boat) {
        // the integrated server's boats share entity ids with the client's
        if (!boat.level().isClientSide() || !isEnabled() || boat.isLocalInstanceAuthoritative()) return null;
        return TRACKS.get(boat.getId());
    }
}
*///? }
