package dev.o7moon.openboatutils.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.o7moon.openboatutils.OpenBoatUtils;
import dev.o7moon.openboatutils.mixin.GameOptionsAccessor;
import dev.o7moon.openboatutils.mixin.KeyBindingAccessor;
import dev.o7moon.openboatutils.network.ServerboundKeybindPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Client side manager for server-defined keybinds (see issue #12).
 *
 * <p>The server defines a set of keybinds over {@code openboatutils:keybinds}; this registers them
 * as real vanilla {@link KeyBinding}s (so they show up and can be rebound in the controls menu),
 * persists the player's per-server remappings, runs any client side action attached to a key, and
 * sends a {@code keybind_event} back to the server on every press and release.
 */
public class Keybinds {

    /** Prefix of every dynamically registered keybind's translation key. */
    public static final String TRANSLATION_PREFIX = "key.openboatutils.";

    /**
     * Controls-menu category our binds live under, localised via the mod's lang file.
     * 1.21.7 turned categories from plain strings into {@code KeyBinding.Category} objects that
     * register (and order) themselves on creation, so this must be created exactly once.
     */
    //? >=1.21.7 {
    /*private static final net.minecraft.client.option.KeyBinding.Category CATEGORY =
            net.minecraft.client.option.KeyBinding.Category.create(net.minecraft.util.Identifier.of(OpenBoatUtils.NAMESPACE, "keybinds"));
    *///? } else {
    private static final String CATEGORY = "key.categories.openboatutils";
    //? }

    /** translationKey -> server label, read by {@code TranslationStorageMixin} (possibly off-thread). */
    public static final Map<String, String> LABELS = new java.util.concurrent.ConcurrentHashMap<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("openboatutils-keybinds.json");

    private static final List<Entry> entries = new ArrayList<>();

    private static final class Entry {
        final KeybindDefinition def;
        final KeyBinding binding;
        boolean lastPressed = false;
        String lastBoundKey;

        Entry(KeybindDefinition def, KeyBinding binding) {
            this.def = def;
            this.binding = binding;
            this.lastBoundKey = binding.getBoundKeyTranslationKey();
        }
    }

    /** Replaces the currently registered keybinds with the ones the server just defined. */
    public static void define(List<KeybindDefinition> definitions) {
        MinecraftClient client = MinecraftClient.getInstance();
        removeAll(client);

        String server = serverKey(client);
        Map<String, Map<String, String>> config = loadConfig();
        Map<String, String> saved = config.computeIfAbsent(server, k -> new HashMap<>());

        // forget remappings for keybinds the server no longer offers
        Set<String> definedIds = new HashSet<>();
        for (KeybindDefinition def : definitions) definedIds.add(def.id());
        saved.keySet().retainAll(definedIds);

        for (KeybindDefinition def : definitions) {
            InputUtil.Key defaultKey = parseKey(def.defaultKey());
            String translationKey = TRANSLATION_PREFIX + def.id();

            // the constructor registers the binding into KeyBinding's static maps
            KeyBinding binding = new KeyBinding(translationKey, defaultKey.getCategory(), defaultKey.getCode(), CATEGORY);

            // keep the player's own remapping if they have one, otherwise use the server default
            String savedKey = saved.get(def.id());
            if (savedKey != null) {
                binding.setBoundKey(parseKey(savedKey));
            }

            LABELS.put(translationKey, def.label());
            entries.add(new Entry(def, binding));
        }

        ensureCategoryOrder();
        rebuildAllKeys(client);
        KeyBinding.updateKeysByCode();

        saveConfig(config);
    }

    /** Removes all of our keybinds, e.g. on disconnect. */
    public static void clear() {
        removeAll(MinecraftClient.getInstance());
        KeyBinding.updateKeysByCode();
    }

    private static void removeAll(MinecraftClient client) {
        Map<String, KeyBinding> byId = KeyBindingAccessor.getKeysById();

        for (Entry e : entries) {
            byId.remove(bindingId(e.binding));
            LABELS.remove(bindingId(e.binding));
        }

        entries.clear();
        rebuildAllKeys(client);
    }

    /** The translation key / id a keybind is registered under (renamed {@code getId} in 1.21.7). */
    private static String bindingId(KeyBinding binding) {
        //? >=1.21.7 {
        /*return binding.getId();
        *///? } else {
        return binding.getTranslationKey();
        //? }
    }

    /** Rebuilds {@code GameOptions.allKeys} so it contains every non-OBU bind plus our current ones. */
    private static void rebuildAllKeys(MinecraftClient client) {
        if (client.options == null) return;

        KeyBinding[] current = client.options.allKeys;
        List<KeyBinding> kept = new ArrayList<>(current.length + entries.size());

        for (KeyBinding kb : current) {
            if (!bindingId(kb).startsWith(TRANSLATION_PREFIX)) kept.add(kb);
        }
        for (Entry e : entries) kept.add(e.binding);

        ((GameOptionsAccessor) (Object) client.options).setAllKeys(kept.toArray(new KeyBinding[0]));
    }

    private static void ensureCategoryOrder() {
        //? >=1.21.7 {
        // KeyBinding.Category registers and orders itself on creation; nothing to do here.
        //? } else {
        Map<String, Integer> order = KeyBindingAccessor.getCategoryOrderMap();

        if (!order.containsKey(CATEGORY)) {
            int max = 0;
            for (int value : order.values()) max = Math.max(max, value);
            order.put(CATEGORY, max + 1);
        }
        //? }
    }

    /** Polled every client tick: detects press/release edges and user rebinds. */
    public static void tick(MinecraftClient client) {
        if (entries.isEmpty()) return;

        boolean canPress = client.player != null && client.currentScreen == null;

        for (Entry e : entries) {
            // a rebind via the controls menu changes the bound key: persist it per-server
            String boundNow = e.binding.getBoundKeyTranslationKey();
            if (!boundNow.equals(e.lastBoundKey)) {
                e.lastBoundKey = boundNow;
                persistRebind(client, e.def.id(), boundNow);
            }

            boolean pressed = canPress && e.binding.isPressed();
            if (pressed != e.lastPressed) {
                e.lastPressed = pressed;
                fire(client, e.def, pressed);
            }
        }
    }

    private static void fire(MinecraftClient client, KeybindDefinition def, boolean pressed) {
        applyAction(pressed ? def.onPress() : def.onRelease());
        sendEvent(client, def.id(), pressed);
    }

    private static void applyAction(KeyAction action) {
        switch (action.type()) {
            case KeyAction.SWITCH_CONTEXT -> {
                if (action.context() != null) {
                    OpenBoatUtils.instance.setActiveContext(OpenBoatUtils.instance.getStoredContext(action.context()));
                }
            }
            case KeyAction.RESET_CONTEXT -> OpenBoatUtils.instance.setActiveContext(null);
            default -> { /* NONE: server only wants the event */ }
        }
    }

    private static void sendEvent(MinecraftClient client, String id, boolean pressed) {
        if (!OpenBoatUtils.KEYBIND_CHANNEL.canSendC2S()) return;

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeShort(ServerboundKeybindPacket.KEYBIND_EVENT.ordinal());
        buf.writeString(id);
        buf.writeBoolean(pressed);
        buf.writeLong(client.world != null ? client.world.getTime() : 0L);

        OpenBoatUtils.KEYBIND_CHANNEL.sendPacketC2S(buf);
    }

    private static void persistRebind(MinecraftClient client, String id, String boundKeyTranslationKey) {
        String server = serverKey(client);
        Map<String, Map<String, String>> config = loadConfig();
        config.computeIfAbsent(server, k -> new HashMap<>()).put(id, boundKeyTranslationKey);
        saveConfig(config);
    }

    private static InputUtil.Key parseKey(@Nullable String translationKey) {
        if (translationKey == null || translationKey.isEmpty()) return InputUtil.UNKNOWN_KEY;

        try {
            return InputUtil.fromTranslationKey(translationKey);
        } catch (Exception e) {
            OpenBoatUtils.LOG.warn("Unknown keybind key '{}', leaving unbound", translationKey);
            return InputUtil.UNKNOWN_KEY;
        }
    }

    private static String serverKey(MinecraftClient client) {
        ServerInfo info = client.getCurrentServerEntry();
        if (info != null && info.address != null && !info.address.isEmpty()) return info.address;
        if (client.isInSingleplayer()) return "singleplayer";
        return "unknown";
    }

    private static Map<String, Map<String, String>> loadConfig() {
        Map<String, Map<String, String>> result = new HashMap<>();

        if (!Files.exists(CONFIG_PATH)) return result;

        try {
            JsonObject root = JsonParser.parseString(Files.readString(CONFIG_PATH, StandardCharsets.UTF_8)).getAsJsonObject();

            for (Map.Entry<String, JsonElement> serverEntry : root.entrySet()) {
                Map<String, String> binds = new HashMap<>();

                for (Map.Entry<String, JsonElement> bind : serverEntry.getValue().getAsJsonObject().entrySet()) {
                    binds.put(bind.getKey(), bind.getValue().getAsString());
                }

                result.put(serverEntry.getKey(), binds);
            }
        } catch (Exception e) {
            OpenBoatUtils.LOG.error("Failed to read keybind config: {}", e.toString());
        }

        return result;
    }

    private static void saveConfig(Map<String, Map<String, String>> config) {
        try {
            JsonObject root = new JsonObject();

            for (Map.Entry<String, Map<String, String>> serverEntry : config.entrySet()) {
                if (serverEntry.getValue().isEmpty()) continue;

                JsonObject binds = new JsonObject();
                for (Map.Entry<String, String> bind : serverEntry.getValue().entrySet()) {
                    binds.addProperty(bind.getKey(), bind.getValue());
                }

                root.add(serverEntry.getKey(), binds);
            }

            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException e) {
            OpenBoatUtils.LOG.error("Failed to write keybind config: {}", e.toString());
        }
    }
}
