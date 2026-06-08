package dev.o7moon.openboatutils.client;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * A client side action the server attaches to a keybind, run instantly when the key is
 * pressed or released (before/alongside the {@code keybind_event} sent back to the server).
 *
 * <p>The server fully defines this behaviour when it sends the keybind definitions; the
 * client only ever runs what it was told to run.
 */
public record KeyAction(byte type, @Nullable Identifier context) {

    /** Do nothing on the client, just send the event to the server. */
    public static final byte NONE = 0;
    /** Instantly switch the active context to {@link #context()}. */
    public static final byte SWITCH_CONTEXT = 1;
    /** Instantly reset the active context to {@code null} (vanilla behaviour). */
    public static final byte RESET_CONTEXT = 2;

    public static final KeyAction NOOP = new KeyAction(NONE, null);
}
