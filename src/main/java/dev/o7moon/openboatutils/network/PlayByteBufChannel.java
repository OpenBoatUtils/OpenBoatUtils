package dev.o7moon.openboatutils.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayByteBufChannel extends ByteBufChannel<ServerPlayerEntity, ServerPlayNetworking.PlayPayloadHandler<ByteBufChannel.BytePayload>, ClientPlayNetworking.PlayPayloadHandler<ByteBufChannel.BytePayload>> {

    public PlayByteBufChannel(Identifier identifier) {
        super(identifier);
    }

    public void registerCodec() {
        PayloadTypeRegistry.playS2C().register(id, codec);
        PayloadTypeRegistry.playC2S().register(id, codec);
    }

    public void registerServerHandler(ServerPlayNetworking.PlayPayloadHandler<BytePayload> handler) {
        ServerPlayNetworking.registerGlobalReceiver(id, handler);
    }

    public void registerClientHandler(ClientPlayNetworking.PlayPayloadHandler<BytePayload> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, handler);
    }

    public void sendPacketC2S(PacketByteBuf byteBuf) {
        ClientPlayNetworking.send(new BytePayload() {
            @Override
            public ByteBuf getData() {
                return byteBuf;
            }

            @Override
            public Id<? extends CustomPayload> getId() {
                return id;
            }
        });
    }

    public void sendPacketS2C(ServerPlayerEntity player, PacketByteBuf byteBuf) {
        ServerPlayNetworking.send(player, new BytePayload() {
            @Override
            public ByteBuf getData() {
                return byteBuf;
            }

            @Override
            public Id<? extends CustomPayload> getId() {
                return id;
            }
        });
    }
}
