# Protocol
OpenBoatUtils uses a feature of the vanilla game called Plugin Channels to communicate

OpenBoatUtils uses three plugin channels:
- [`openboatutils:settings`](/developers/settings) for all boat settings
- [`openboatutils:context`](/developers/context) for context operations (introduced in `0.5.0`)
- [`openboatutils:configuration`](/developers/configuration) for configuration phase versioning.

The context api is optional, ignoring it will produce identical behaviour to before the introduction.

::: info A Note on Strings
OpenBoatUtils writes all it's packets with a class in Minecraft called `PacketByteBuf`.
Spigot based servers don't expose this class, so usually a java standard library class called `DataOutputStream` is used instead.

This works well for the most part, however it does not write strings in the minecraft-protocol-way that `PacketByteBuf` does.
`PacketByteBuf` writes string length as a [VarInt](https://minecraft.wiki/w/Java_Edition_protocol/Data_types#VarInt_and_VarLong), while `DataOutputStream`'s `writeUTF(...)` uses a fixed length short.

Here is a small function which writes a string to a `DataOutputStream` in a way that is compatible with `PacketByteBuf`
```java
public void writeString(DataOutputStream out, String stringValue) throws IOException {
    int length = stringValue.length();

    // write the length as a varint
    while (true) {
        if(((length & ~0x7F)) == 0) {
            out.writeByte(length);
            break;
        }

        out.writeByte((length & 0x7F) | 0x80);
        length >>>= 7;
    }

    // write the bytes of the string
    out.writeBytes(stringValue);
}
```
::: details Another implementation using a wrapper class
```java
public class PacketByteBuf {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

    public PacketByteBuf writeFloat(float v) throws IOException { out.writeFloat(v); return this; }
    public PacketByteBuf writeBoolean(boolean v) throws IOException { out.writeBoolean(v); return this; }
    public PacketByteBuf writeDouble(double v) throws IOException { out.writeDouble(v); return this; }
    public PacketByteBuf writeShort(short v) throws IOException { out.writeShort(v); return this; }
    public PacketByteBuf writeInt(int v) throws IOException { out.writeInt(v); return this; }
    public PacketByteBuf writeByte(byte v) throws IOException { out.writeByte(v); return this; }

    public PacketByteBuf writeString(String s) throws IOException {
        int len = s.length();
        while (true) {
            if ((len & ~0x7F) == 0) { out.writeByte(len); break; }
            out.writeByte((len & 0x7F) | 0x80);
            len >>>= 7;
        }
        out.writeBytes(s);

        return this;
    }

    public byte[] toBytes() { return byteArrayOutputStream.toByteArray(); }
}

```
:::