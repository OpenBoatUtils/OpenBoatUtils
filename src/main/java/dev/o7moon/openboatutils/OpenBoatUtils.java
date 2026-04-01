package dev.o7moon.openboatutils;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenBoatUtils extends SettingContext implements ModInitializer {

    public static final String NAMESPACE = "openboatutils";
    public static final int VERSION = 19;
    public static final boolean UNSTABLE = true;

    public static final Logger LOG = LoggerFactory.getLogger("OpenBoatUtils");
    public static final Identifier settingsChannel = Identifier.of(NAMESPACE,"settings");

    public static OpenBoatUtils instance;

    public static boolean enabled = false;

    // non-context settings, don't reset with the rest but reset when joining a server (could persist on proxies)
    // there is a separate reset packet that includes these, but the original ones are for resetting the
    // active context rather than the entire state of the mod.
    // (08/26/25) there is actually not a separate reset packet at the moment. TODO

    public static boolean interpolationCompat = false;

    public OpenBoatUtils() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        ClientboundPackets.registerCodecs();
        ServerboundPackets.registerCodecs();

        ServerboundPackets.registerHandlers();

        SingleplayerCommands.registerCommands();
    }

    public void resetAll() {
        // reset the default context
        resetSettings();

        // reset additional non-context state
        interpolationCompat = false;
    }

    public void resetSettings() {
        OpenBoatUtils.enabled = false;
        OpenBoatUtils.instance.applyFrom(ISettingContext.VANILLA);
    }

    public static void sendVersionPacket(){
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeShort(ServerboundPackets.VERSION.ordinal());
        packet.writeInt(VERSION);
        packet.writeBoolean(UNSTABLE);
        sendPacketC2S(packet);
    }

    public record BytePayload(ByteBuf data) implements CustomPayload {
        public static final PacketCodec<PacketByteBuf, BytePayload> CODEC = CustomPayload.codecOf(BytePayload::write, BytePayload::new);
        public static final Id<BytePayload> ID = new Id<>(settingsChannel);

        public BytePayload(PacketByteBuf buf) {
            this(buf.copy());
            buf.readerIndex(buf.writerIndex());// so mc doesn't complain we haven't read all the bytes
        }

        void write(PacketByteBuf buf) {
            buf.writeBytes(data);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void sendPacketC2S(PacketByteBuf packet) {
        BytePayload payload = new BytePayload(packet);
        ClientPlayNetworking.send(payload);
    }

    public static void sendPacketS2C(ServerPlayerEntity player, PacketByteBuf packet){
        BytePayload payload = new BytePayload(packet);
        ServerPlayNetworking.send(player, payload);
    }
    // doesn't deal with .enabled because its a non-context setting that is for the general runtime of obu
    // and not a specific client boat
    public static void setInterpolationCompat(boolean interpolationCompat) {
        OpenBoatUtils.interpolationCompat = interpolationCompat;
    }

    public ISettingContext getDefaultContext() {
        return this;
    }
}
