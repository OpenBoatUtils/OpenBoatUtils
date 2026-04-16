package dev.o7moon.openboatutils.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ConfigurationByteBufChannel extends ByteBufChannel<ServerConfigurationNetworkHandler, ServerConfigurationNetworking.ConfigurationPacketHandler<ByteBufChannel.BytePayload>, ClientConfigurationNetworking.ConfigurationPayloadHandler<ByteBufChannel.BytePayload>> {

    public ConfigurationByteBufChannel(Identifier identifier) {
        super(identifier);
    }

    public void registerCodec() {
        PayloadTypeRegistry.configurationS2C().register(id, codec);
        PayloadTypeRegistry.configurationC2S().register(id, codec);
    }

    public void registerServerHandler(ServerConfigurationNetworking.ConfigurationPacketHandler<BytePayload> handler) {
        ServerConfigurationNetworking.registerGlobalReceiver(id, handler);
    }

    public void registerClientHandler(ClientConfigurationNetworking.ConfigurationPayloadHandler<BytePayload> handler) {
        ClientConfigurationNetworking.registerGlobalReceiver(id, handler);
    }

    public void sendPacketC2S(PacketByteBuf byteBuf) {
        ClientConfigurationNetworking.send(new BytePayload() {
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

    public void sendPacketS2C(ServerConfigurationNetworkHandler player, PacketByteBuf byteBuf) {
        ServerConfigurationNetworking.send(player, new BytePayload() {
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
