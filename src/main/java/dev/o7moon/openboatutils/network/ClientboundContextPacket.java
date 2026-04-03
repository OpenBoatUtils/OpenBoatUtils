package dev.o7moon.openboatutils.network;

import dev.o7moon.openboatutils.ISettingContext;
import dev.o7moon.openboatutils.OpenBoatUtils;
import dev.o7moon.openboatutils.StoredContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public enum ClientboundContextPacket {
    SWITCH_CONTEXT,
    DROP_CONTEXT;

    public static void handlePacket(PacketByteBuf buf) {
        try {
            short packetID = buf.readShort();

            ClientboundContextPacket[] packets = ClientboundContextPacket.values();

            if (packetID >= packets.length) return;

            ClientboundContextPacket packet = packets[packetID];

            switch (packet) {
                case SWITCH_CONTEXT -> {
                    Identifier identifier = Identifier.of(buf.readString());

                    ISettingContext context = OpenBoatUtils.instance.getStoredContext(identifier);

                    if (OpenBoatUtils.instance.getActiveContext() == context) return;

                    OpenBoatUtils.instance.setActiveContext(context);
                }
                case DROP_CONTEXT -> {
                    if (OpenBoatUtils.instance.getActiveContext() instanceof StoredContext active) {
                        OpenBoatUtils.instance.dropStoredContext(active.getIdentifier());
                    }

                    OpenBoatUtils.instance.setActiveContext(OpenBoatUtils.instance);
                }
            }
        } catch (Exception E) {
            OpenBoatUtils.LOG.error("Error when handling clientbound openboatutils packet: ");
            for (StackTraceElement e : E.getStackTrace()){
                OpenBoatUtils.LOG.error(e.toString());
            }
        }
    }
}
