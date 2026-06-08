package dev.o7moon.openboatutils.network;

import dev.o7moon.openboatutils.OpenBoatUtils;
import dev.o7moon.openboatutils.client.KeyAction;
import dev.o7moon.openboatutils.client.KeybindDefinition;
import dev.o7moon.openboatutils.client.Keybinds;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public enum ClientboundKeybindPacket {
    DEFINE_KEYBINDS,
    CLEAR_KEYBINDS;

    public static void handlePacket(PacketByteBuf buf) {
        try {
            short packetID = buf.readShort();

            ClientboundKeybindPacket[] packets = ClientboundKeybindPacket.values();

            if (packetID >= packets.length) return;

            switch (packets[packetID]) {
                case DEFINE_KEYBINDS -> {
                    int count = buf.readInt();

                    List<KeybindDefinition> definitions = new ArrayList<>(Math.max(0, count));

                    for (int i = 0; i < count; i++) {
                        String id = buf.readString();
                        String label = buf.readString();
                        String defaultKey = buf.readString();
                        KeyAction onPress = readAction(buf);
                        KeyAction onRelease = readAction(buf);

                        definitions.add(new KeybindDefinition(id, label, defaultKey, onPress, onRelease));
                    }

                    Keybinds.define(definitions);
                }
                case CLEAR_KEYBINDS -> Keybinds.clear();
            }
        } catch (Exception E) {
            OpenBoatUtils.LOG.error("Error when handling clientbound openboatutils keybind packet: ");
            for (StackTraceElement e : E.getStackTrace()) {
                OpenBoatUtils.LOG.error(e.toString());
            }
        }
    }

    private static KeyAction readAction(PacketByteBuf buf) {
        byte type = buf.readByte();

        if (type == KeyAction.SWITCH_CONTEXT) {
            return new KeyAction(type, Identifier.of(buf.readString()));
        }

        return new KeyAction(type, null);
    }
}
