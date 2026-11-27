package dev.o7moon.openboatutils.sync;

//? >= 26.1 {
/*import dev.o7moon.openboatutils.OpenBoatUtils;
import io.github.openboatutils.protocol.channels.OBUSyncPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Server side of openboatutils:sync: a dumb relay, run on the server thread.
// The entity id always comes from the sender's own vehicle.
public final class SyncRelay {
    private SyncRelay() {}

    // a driver sends 20 states and about 8 probes a second
    private static final int MAX_PACKETS_PER_SECOND = 40;

    // sender -> {window start nanos, packets in window}
    private static final Map<UUID, long[]> RATE = new HashMap<>();

    public static void relay(ServerPlayer sender, ByteBuf data) {
        int length = data.readableBytes();
        if (length < 2) return;

        short packetId = data.getShort(data.readerIndex());
        boolean valid = (packetId == 0 && length == OBUSyncPacket.STATE_LENGTH)
                || (packetId == 1 && length == OBUSyncPacket.PROBE_LENGTH);
        if (!valid) return;

        if (!allow(sender)) return;

        Entity vehicle = sender.getVehicle();
        if (!(vehicle instanceof AbstractBoat) || vehicle.getControllingPassenger() != sender) return;

        int vehicleId = vehicle.getId();

        // only players who can see the boat
        for (ServerPlayer player : PlayerLookup.tracking(vehicle)) {
            if (player == sender || player.connection == null) continue;
            if (!ServerPlayNetworking.canSend(player, OpenBoatUtils.SYNC_CHANNEL.getId())) continue;

            FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer(4 + length));
            out.writeInt(vehicleId);
            out.writeBytes(data, data.readerIndex(), length);

            OpenBoatUtils.SYNC_CHANNEL.sendPacketS2C(player, out);
        }
    }

    private static boolean allow(ServerPlayer sender) {
        long now = System.nanoTime();
        long[] window = RATE.computeIfAbsent(sender.getUUID(), unused -> new long[]{now, 0});

        if (now - window[0] >= 1_000_000_000L) {
            window[0] = now;
            window[1] = 0;
        }

        return ++window[1] <= MAX_PACKETS_PER_SECOND;
    }

    public static void forget(ServerPlayer player) {
        RATE.remove(player.getUUID());
    }
}
*///? }
