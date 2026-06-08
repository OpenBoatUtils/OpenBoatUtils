package dev.o7moon.openboatutils.client;

/**
 * A single keybind defined by the server.
 *
 * @param id         unique identifier of the keybind, echoed back in {@code keybind_event}
 * @param label      human readable name shown in the vanilla controls menu
 * @param defaultKey default key as an {@code InputUtil} translation key (e.g. {@code key.keyboard.g})
 * @param onPress    client side action run when the key is pressed
 * @param onRelease  client side action run when the key is released
 */
public record KeybindDefinition(
        String id,
        String label,
        String defaultKey,
        KeyAction onPress,
        KeyAction onRelease
) {}
