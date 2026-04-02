# Protocol
OpenBoatUtils uses a feature of the vanilla game called Plugin Channels to communicate
Currently, all packets are sent on the `openboatutils:settings` channel.



::: info A Note on Strings
OpenBoatUtils writes all it's packets with a class in Minecraft called `PacketByteBuf`. Server plugin API's don't expose this class, so usually a java standard library class called `DataOutputStream` is used instead. this works well for the most part, however it does not write strings in the minecraft-protocol-way that `PacketByteBuf` does. `PacketByteBuf` writes string length as a [VarInt](https://wiki.vg/VarInt_And_VarLong), while `DataOutputStream`'s `writeUTF(...)` uses a fixed length short.

here is a small function which writes a string to a `DataOutputStream` in a way that is compatible with `PacketByteBuf`:
```java
private static final int SEGMENT_BITS = 0x7F;
private static final int CONTINUE_BIT = 0x80;

public void writeString(DataOutputStream out, String stringValue) throws IOException {
    int length = stringValue.length();

    // write the length as a varint
    while (true) {
        if(((length & ~SEGMENT_BITS)) == 0) {
            out.writeByte(length);
            break;
        }

        out.writeByte((length & SEGMENT_BITS) | CONTINUE_BIT);
        length >>>= 7;
    }

    // write the bytes of the string
    out.writeBytes(stringValue);
}
```
:::