package io.github.openboatutils.protocol.channels;

import io.github.openboatutils.protocol.OBUChannel;
import io.github.openboatutils.protocol.OBUPacket;
import io.github.openboatutils.protocol.PacketWriter;

import java.io.IOException;

public sealed interface OBUSyncPacket extends OBUPacket permits
        OBUSyncPacket.State,
        OBUSyncPacket.Probe,
        OBUSyncPacket.Relayed {

    // packet id short + body
    int STATE_LENGTH = 2 + 8 + 4 + 3 * 8 + 3 * 4 + 4 + 4 + 4 + 4 + 2 * 4 + 1;
    int PROBE_LENGTH = 2 + 8 + 4;

    @Override default short getPacketId() {
        throw new RuntimeException("Not Implemented");
    }
    @Override default short getProtocolVersion() { throw new RuntimeException("Not Implemented"); }

    @Override
    default OBUChannel getChannel() {
        return OBUChannel.SYNC;
    }

    // Serverbound, sent by the driver at the end of every boat tick.
    final class State implements OBUSyncPacket {
        public long stampNanos;
        public float halfRttMs;
        public double x, y, z;
        public float vx, vy, vz;
        public float yaw;
        public float yawRate;
        public float yawAccel;
        public float friction;
        public float ax, az;
        public byte flags;

        public State() {}

        public short getVersion() { return 23; }
        public short getPacketId() { return 0; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSyncPacket.super.write(writer);
            writer.writeLong(stampNanos);
            writer.writeFloat(halfRttMs);
            writer.writeDouble(x);
            writer.writeDouble(y);
            writer.writeDouble(z);
            writer.writeFloat(vx);
            writer.writeFloat(vy);
            writer.writeFloat(vz);
            writer.writeFloat(yaw);
            writer.writeFloat(yawRate);
            writer.writeFloat(yawAccel);
            writer.writeFloat(friction);
            writer.writeFloat(ax);
            writer.writeFloat(az);
            writer.writeByte(flags);
        }
    }

    // Serverbound, sent by the driver about 8 times a second between ticks, spread over the tick phase.
    final class Probe implements OBUSyncPacket {
        public long stampNanos;
        public float halfRttMs;

        public Probe() {}
        public Probe(long stampNanos, float halfRttMs) {
            this.stampNanos = stampNanos;
            this.halfRttMs = halfRttMs;
        }

        public short getVersion() { return 23; }
        public short getPacketId() { return 1; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSyncPacket.super.write(writer);
            writer.writeLong(stampNanos);
            writer.writeFloat(halfRttMs);
        }
    }

    // Clientbound, a serverbound sync packet relayed to the other players unchanged,
    // prefixed with the entity id of the sender's vehicle.
    final class Relayed implements OBUSyncPacket {
        public int vehicleEntityId;
        public OBUSyncPacket packet;

        public Relayed() {}
        public Relayed(int vehicleEntityId, OBUSyncPacket packet) {
            this.vehicleEntityId = vehicleEntityId;
            this.packet = packet;
        }

        public short getVersion() { return 23; }
        public short getPacketId() { return packet.getPacketId(); }

        @Override
        public void write(PacketWriter writer) throws IOException {
            writer.writeInt(vehicleEntityId);
            packet.write(writer);
        }
    }
}
