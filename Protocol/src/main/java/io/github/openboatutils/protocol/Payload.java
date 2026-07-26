package io.github.openboatutils.protocol;

import java.io.IOException;

public interface Payload {
    void write(PacketWriter writer) throws IOException;
}
