package io.github.openboatutils.protocol.channels;

import io.github.openboatutils.protocol.OBUChannel;
import io.github.openboatutils.protocol.OBUPacket;
import io.github.openboatutils.protocol.PacketWriter;

import java.io.IOException;
import java.util.UUID;

public sealed interface OBUContextPacket extends OBUPacket permits
        OBUContextPacket.Reset,
        OBUContextPacket.Switch,
        OBUContextPacket.Drop,
        OBUContextPacket.Store,
        OBUContextPacket.Entity {

    @Override default short getPacketId() {
        throw new RuntimeException("Not Implemented");
    }
    @Override default short getProtocolVersion() { throw new RuntimeException("Not Implemented"); }

    @Override
    default OBUChannel getChannel() {
        return OBUChannel.CONTEXT;
    };

    final class Reset implements OBUContextPacket {
        public Reset() {}

        public short getVersion() { return 19; }
        public short getPacketId() { return 0; }
    }

    final class Switch implements OBUContextPacket {
        public String key;

        public Switch() {}
        public Switch(String key) { this.key = key; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 1; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUContextPacket.super.write(writer);
            writer.writeString(key);
        }
    }

    final class Drop implements OBUContextPacket {
        public String key;

        public Drop() {}
        public Drop(String key) { this.key = key; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 2; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUContextPacket.super.write(writer);
            writer.writeString(key);
        }
    }

    final class Store implements OBUContextPacket {
        public String key;
        public OBUSettingsPacket.CompoundPayload compound;

        public Store() {}
        public Store(String key, OBUSettingsPacket.CompoundPayload compound) { this.key = key; this.compound = compound; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 3; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUContextPacket.super.write(writer);
            writer.writeString(key);
            compound.write(writer);
        }
    }

    final class Entity implements OBUContextPacket {
        public UUID uuid;
        public OBUSettingsPacket.CompoundPayload compound;

        public Entity() {}
        public Entity(UUID uuid, OBUSettingsPacket.CompoundPayload compound) { this.uuid = uuid; this.compound = compound; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 4; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUContextPacket.super.write(writer);
            writer.writeString(uuid.toString());
            compound.write(writer);
        }
    }
}