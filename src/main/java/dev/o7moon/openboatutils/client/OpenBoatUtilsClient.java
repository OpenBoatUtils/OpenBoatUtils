package dev.o7moon.openboatutils.client;

import dev.o7moon.openboatutils.OpenBoatUtils;
import dev.o7moon.openboatutils.network.ClientboundContextPacket;
import dev.o7moon.openboatutils.network.ClientboundSettingsPacket;
import dev.o7moon.openboatutils.network.ServerboundSettingsPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;

import static dev.o7moon.openboatutils.OpenBoatUtils.CONTEXT_CHANNEL;
import static dev.o7moon.openboatutils.OpenBoatUtils.SETTING_CHANNEL;

public class OpenBoatUtilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SETTING_CHANNEL.registerClientHandler((bytePayload, context) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.wrappedBuffer(bytePayload.getData()));
            context.client().execute(() -> {
                ClientboundSettingsPacket.handlePacket(buf);
                buf.release();
            });
        });

        CONTEXT_CHANNEL.registerClientHandler((bytePayload, context) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.wrappedBuffer(bytePayload.getData()));
            context.client().execute(() -> {
                ClientboundContextPacket.handlePacket(buf);
                buf.release();
            });
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            OpenBoatUtils.instance.resetAll();
            OpenBoatUtils.sendVersionPacket();
        });
    }
}
