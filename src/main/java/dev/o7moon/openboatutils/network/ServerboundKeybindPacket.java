package dev.o7moon.openboatutils.network;

import dev.o7moon.openboatutils.OpenBoatUtils;
import net.minecraft.network.PacketByteBuf;

public enum ServerboundKeybindPacket {
    KEYBIND_EVENT;

    /**
     * Handles a keybind packet received by a fabric server. The intended consumer of
     * {@code keybind_event} is the server software (e.g. a plugin reading the raw channel);
     * this only exists so a fabric server does not choke on the channel and for debugging.
     */
    public static void handlePacket(PacketByteBuf buf) {
        try {
            short packetID = buf.readShort();

            if (packetID != KEYBIND_EVENT.ordinal()) return;

            String id = buf.readString();
            boolean pressed = buf.readBoolean();
            long tick = buf.readLong();

            OpenBoatUtils.LOG.debug("Keybind event received by server: {} {} @ tick {}", id, pressed ? "PRESS" : "RELEASE", tick);
        } catch (Exception E) {
            OpenBoatUtils.LOG.error("Error when handling serverbound openboatutils keybind packet: ");
            for (StackTraceElement e : E.getStackTrace()) {
                OpenBoatUtils.LOG.error(e.toString());
            }
        }
    }
}
