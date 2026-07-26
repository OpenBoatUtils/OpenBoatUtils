package io.github.openboatutils.protocol.impl;

import io.github.openboatutils.protocol.PacketWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataOutputStreamWriter implements PacketWriter {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

    public byte[] toBytes() { return byteArrayOutputStream.toByteArray(); }

    @Override public void writeFloat(float v) throws IOException { out.writeFloat(v); }
    @Override public void writeDouble(double v) throws IOException { out.writeDouble(v); }
    @Override public void writeBoolean(boolean v) throws IOException { out.writeBoolean(v); }
    @Override public void writeShort(short v) throws IOException { out.writeShort(v); }
    @Override public void writeByte(byte v) throws IOException { out.writeByte(v); }
    @Override public void writeInt(int v) throws IOException { out.writeInt(v); }
    @Override public void writeLong(long v) throws IOException { out.writeLong(v); }

    public void writeString(String s) throws IOException {
        int len = s.length();
        while (true) {
            if ((len & ~0x7F) == 0) { out.writeByte(len); break; }
            out.writeByte((len & 0x7F) | 0x80);
            len >>>= 7;
        }
        out.writeBytes(s);
    }
}
