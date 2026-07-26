package io.github.openboatutils.protocol;

import java.io.IOException;

public interface OBUPacket extends Payload {
    short getPacketId();
    short getProtocolVersion();
    OBUChannel getChannel();

    default void write(PacketWriter writer) throws IOException {
        writer.writeShort(getPacketId());
    }

    final class TransactionPacket implements OBUPacket {

        private int id;
        private OBUPacket payload;

        @Override
        public OBUChannel getChannel() {
            return payload.getChannel();
        }

        public TransactionPacket() {}
        public TransactionPacket(int id, OBUPacket payload) {
            this.id = id;
            this.payload = payload;
        }

        @Override public short getPacketId() { return Short.MAX_VALUE; }
        @Override public short getProtocolVersion() { return 22; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUPacket.super.write(writer);
            writer.writeInt(id);
            payload.write(writer);
        }
    }
}
