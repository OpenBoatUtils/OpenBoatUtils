package dev.o7moon.openboatutils;

import io.netty.buffer.ByteBuf;
//? >=1.21
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public enum ServerboundPackets {
    VERSION;

    public static void registerCodecs() {
        PayloadTypeRegistry.playC2S().register(OpenBoatUtils.BytePayload.ID, OpenBoatUtils.BytePayload.CODEC);
    }

    public static void registerHandlers(){
        ServerPlayNetworking.registerGlobalReceiver(OpenBoatUtils.BytePayload.ID, ((payload, context) ->
                context.server().execute(() ->
                        handlePacket(payload.data()) )));
    }

    public static void handlePacket(ByteBuf buf) {
        try {
            short packetID = buf.readShort();
            switch (packetID) {
                case 0:
                    int versionID = buf.readInt();
                    OpenBoatUtils.LOG.info("OpenBoatUtils version received by server: "+versionID);
            }
        } catch (Exception E) {
            OpenBoatUtils.LOG.error("Error when handling serverbound openboatutils packet: ");
            for (StackTraceElement e : E.getStackTrace()){
                OpenBoatUtils.LOG.error(e.toString());
            }
        }
    }
}
