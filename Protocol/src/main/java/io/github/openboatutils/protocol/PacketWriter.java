package io.github.openboatutils.protocol;

import java.io.IOException;

public interface PacketWriter {

    byte[] toBytes();

    void writeFloat(float v) throws IOException;
    void writeBoolean(boolean v) throws IOException;
    void writeDouble(double v) throws IOException;
    void writeShort(short v) throws IOException;
    void writeInt(int v) throws IOException;
    void writeLong(long v) throws IOException;
    void writeByte(byte v) throws IOException;
    void writeString(String s) throws IOException;
}
