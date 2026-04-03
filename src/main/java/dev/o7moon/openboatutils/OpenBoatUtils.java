package dev.o7moon.openboatutils;

import dev.o7moon.openboatutils.network.ByteBufChannel;
import dev.o7moon.openboatutils.network.ServerboundSettingsPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OpenBoatUtils extends MutableContext implements ModInitializer {

    public static final String NAMESPACE = "openboatutils";
    public static final int VERSION = 19;
    public static final boolean UNSTABLE = true;

    public static final Logger LOG = LoggerFactory.getLogger("OpenBoatUtils");

    public static final ByteBufChannel SETTING_CHANNEL = new ByteBufChannel(Identifier.of(NAMESPACE, "settings"));
    public static final ByteBufChannel CONTEXT_CHANNEL = new ByteBufChannel(Identifier.of(NAMESPACE, "context"));

    public static final Identifier DEFAULT_CONTEXT = Identifier.of(NAMESPACE, "default");

    public static OpenBoatUtils instance;

    private final Map<Identifier, StoredContext> stored_contexts = new HashMap<>();
    private final Map<UUID, EntityContext> entity_contexts = new HashMap<>();

    private boolean interpolationCompatibility = false;

    private @Nullable ISettingContext activeContext = null;

    public OpenBoatUtils() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        SETTING_CHANNEL.registerCodec();
        CONTEXT_CHANNEL.registerCodec();

        SETTING_CHANNEL.registerServerHandler((bytePayload, context) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.wrappedBuffer(bytePayload.getData()));

            context.server().execute(() -> {
                ServerboundSettingsPacket.handlePacket(buf);
                buf.release();
            });
        });

        SingleplayerCommands.registerCommands();
    }

    public void resetAll() {
        setActiveContext(this);

        interpolationCompatibility = false;
    }

    public static void sendVersionPacket() {
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeShort(ServerboundSettingsPacket.VERSION.ordinal());
        packet.writeInt(VERSION);
        packet.writeBoolean(UNSTABLE);

        SETTING_CHANNEL.sendPacketC2S(packet);
    }

    @Override
    public void switchTo() {
        OpenBoatUtils.instance.applyFrom(ISettingContext.VANILLA);
    }

    public @NotNull ISettingContext getStoredContext(Identifier identifier) {
        if (identifier.equals(DEFAULT_CONTEXT)) return this;

        return stored_contexts.computeIfAbsent(identifier, StoredContext::new);
    }

    public @Nullable EntityContext getEntityContext(UUID uuid) {
        return entity_contexts.get(uuid);
    }

    public void putEntityContext(UUID uuid, @NotNull EntityContext context) {
        entity_contexts.put(uuid, context);
    }

    public @Nullable StoredContext dropStoredContext(Identifier identifier) {
        return stored_contexts.remove(identifier);
    }

    public void putStoredContext(Identifier identifier, @NotNull StoredContext context) {
        stored_contexts.put(identifier, context);
    }

    public void setInterpolationCompatibility(boolean interpolationCompatibility) {
        this.interpolationCompatibility = interpolationCompatibility;
    }

    public boolean getInterpolationCompatibility() {
        return interpolationCompatibility;
    }

    public void setActiveContext(@Nullable ISettingContext context) {
        if (context != null) context.switchTo();
        this.activeContext = context;
    }

    public void dropAllContextStores() {
        stored_contexts.clear();
        entity_contexts.clear();
    }

    public @Nullable ISettingContext getActiveContext() {
        @Nullable ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null && player.getVehicle() instanceof VehicleEntity vehicle) {
            @Nullable ISettingContext entityContext = getEntityContext(vehicle.getUuid());

            if (entityContext != null) {
                return entityContext;
            }
        }

        return activeContext;
    }
}
