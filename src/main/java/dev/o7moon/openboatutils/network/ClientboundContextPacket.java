package dev.o7moon.openboatutils.network;

import dev.o7moon.openboatutils.EntityContext;
import dev.o7moon.openboatutils.ISettingContext;
import dev.o7moon.openboatutils.OpenBoatUtils;
import dev.o7moon.openboatutils.StoredContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public enum ClientboundContextPacket {
    RESET_CONTEXT,
    SWITCH_CONTEXT,
    DROP_CONTEXT,
    STORE_CONTEXT,
    ENTITY_CONTEXT;

    public static void handlePacket(PacketByteBuf buf) {
        try {
            short packetID = buf.readShort();

            ClientboundContextPacket[] packets = ClientboundContextPacket.values();

            if (packetID >= packets.length) return;

            ClientboundContextPacket packet = packets[packetID];

            switch (packet) {
                case RESET_CONTEXT -> {
                    OpenBoatUtils.instance.setActiveContext(null);
                }
                case SWITCH_CONTEXT -> {
                    Identifier identifier = Identifier.of(buf.readString());

                    if (identifier.getNamespace().equals(OpenBoatUtils.NAMESPACE)) {
                        if (!identifier.equals(OpenBoatUtils.DEFAULT_CONTEXT)) {
                            return;
                        }
                    }

                    ISettingContext context = OpenBoatUtils.instance.getStoredContext(identifier);

                    OpenBoatUtils.instance.setActiveContext(context);
                }
                case DROP_CONTEXT -> {
                    Identifier identifier = Identifier.of(buf.readString());

                    @Nullable ISettingContext context = OpenBoatUtils.instance.dropStoredContext(identifier);

                    if (context != null && context == OpenBoatUtils.instance.getActiveContext()) {
                        OpenBoatUtils.instance.setActiveContext(null);
                    }
                }
                case STORE_CONTEXT -> {
                    Identifier identifier = Identifier.of(buf.readString());

                    StoredContext storedContext = new StoredContext(identifier);

                    ClientboundSettingsPacket.handleContextPacket(storedContext, buf, ClientboundSettingsPacket.TRANSACTION);

                    OpenBoatUtils.instance.putStoredContext(identifier, storedContext);
                }
                case ENTITY_CONTEXT -> {
                    UUID id = UUID.fromString(buf.readString());

                    EntityContext entityContext = new EntityContext(id);

                    ClientboundSettingsPacket.handleContextPacket(entityContext, buf, ClientboundSettingsPacket.TRANSACTION);

                    OpenBoatUtils.instance.putEntityContext(id, entityContext);
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
