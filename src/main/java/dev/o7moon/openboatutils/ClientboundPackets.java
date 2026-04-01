package dev.o7moon.openboatutils;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Optional;

public enum ClientboundPackets {
    RESET,
    SET_STEP_HEIGHT,
    SET_DEFAULT_SLIPPERINESS,
    SET_BLOCKS_SLIPPERINESS,
    SET_BOAT_FALL_DAMAGE,
    SET_BOAT_WATER_ELEVATION,
    SET_AIR_CONTROL,
    SET_BOAT_JUMP_FORCE,
    SET_MODE,
    SET_GRAVITY,
    SET_YAW_ACCEL,
    SET_FORWARD_ACCEL,
    SET_BACKWARD_ACCEL,
    SET_TURN_ACCEL,
    ALLOW_ACCEL_STACKING,
    RESEND_VERSION,
    SET_UNDERWATER_CONTROL,
    SET_SURFACE_WATER_CONTROL,
    SET_EXCLUSIVE_MODE,
    SET_COYOTE_TIME,
    SET_WATER_JUMPING,
    SET_SWIM_FORCE,
    REMOVE_BLOCKS_SLIPPERINESS,
    CLEAR_SLIPPERINESS,
    MODE_SERIES,
    EXCLUSIVE_MODE_SERIES,
    SET_PER_BLOCK,
    SET_COLLISION_MODE,
    SET_STEP_WHILE_FALLING,
    SET_INTERPOLATION_COMPAT,
    SET_COLLISION_RESOLUTION,
    ADD_COLLISION_ENTITYTYPE_FILTER,
    CLEAR_COLLISION_ENTITYTYPE_FILTER;

    public static void registerCodecs() {
        PayloadTypeRegistry.playS2C().register(OpenBoatUtils.BytePayload.ID, OpenBoatUtils.BytePayload.CODEC);
    }

    public static void registerHandlers(){
        ClientPlayNetworking.registerGlobalReceiver(OpenBoatUtils.BytePayload.ID, ((payload, context) ->
                context.client().execute(() ->
                    handlePacket(new PacketByteBuf(Unpooled.wrappedBuffer(payload.data()))) )));
    }

    public static void handlePacket(PacketByteBuf buf) {
        try {
            // this is fine for now, must update when other contexts exist
            SettingContext context = OpenBoatUtils.instance;
            
            short packetID = buf.readShort();

            ClientboundPackets[] packets = ClientboundPackets.values();

            if (packetID >= packets.length) return;

            switch (packets[packetID]) {
                case RESET -> {
                    OpenBoatUtils.enabled = false;
                    context.applyFrom(ISettingContext.VANILLA);
                }
                case SET_STEP_HEIGHT -> {
                    float stepSize = buf.readFloat();
                    context.setStepSize(stepSize);
                    OpenBoatUtils.enabled = true;
                }
                case SET_DEFAULT_SLIPPERINESS -> {
                    context.setDefaultSlipperiness(buf.readFloat());
                    OpenBoatUtils.enabled = true;
                }
                case SET_BLOCKS_SLIPPERINESS -> {
                    float slipperiness = buf.readFloat();

                    Arrays.stream(buf.readString().split(","))
                            .map(Identifier::of)
                            .forEach(block -> context.setBlockSlipperiness(block, slipperiness));
                    OpenBoatUtils.enabled = true;
                }
                case SET_BOAT_FALL_DAMAGE -> {
                    context.setFallDamage(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case SET_BOAT_WATER_ELEVATION -> {
                    context.setWaterElevation(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case SET_AIR_CONTROL -> {
                    context.setAirControl(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case SET_BOAT_JUMP_FORCE -> {
                    context.setJumpForce(buf.readFloat());
                    OpenBoatUtils.enabled = true;
                }
                case SET_MODE -> {
                    Modes.setMode(Modes.values()[buf.readShort()]);
                    OpenBoatUtils.enabled = true;
                }
                case SET_GRAVITY -> {
                    context.setGravityForce(buf.readDouble());
                    OpenBoatUtils.enabled = true;
                }
                case SET_YAW_ACCEL -> {
                    context.setYawAccel(buf.readFloat());
                    OpenBoatUtils.enabled = true;
                }
                case SET_FORWARD_ACCEL -> {
                    context.setForwardAccel(buf.readFloat());
                    OpenBoatUtils.enabled = true;
                }
                case SET_BACKWARD_ACCEL -> {
                    context.setBackwardAccel(buf.readFloat());
                    OpenBoatUtils.enabled = true;
                }
                case SET_TURN_ACCEL -> {
                    context.setTurnForwardAccel(buf.readFloat());
                    OpenBoatUtils.enabled = true;
                }
                case ALLOW_ACCEL_STACKING -> {
                    context.setAllowAccelStacking(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case RESEND_VERSION -> {
                    OpenBoatUtils.sendVersionPacket();
                    OpenBoatUtils.enabled = true;
                }
                case SET_UNDERWATER_CONTROL -> {
                    context.setUnderwaterControl(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case SET_SURFACE_WATER_CONTROL -> {
                    context.setSurfaceWaterControl(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case SET_EXCLUSIVE_MODE -> {
                    short mode = buf.readShort();
                    context.applyFrom(ISettingContext.VANILLA);
                    Modes.setMode(Modes.values()[mode]);
                    OpenBoatUtils.enabled = true;
                }
                case SET_COYOTE_TIME -> {
                    context.setCoyoteTime(buf.readInt());
                    OpenBoatUtils.enabled = true;
                }
                case SET_WATER_JUMPING -> {
                    context.setWaterJumping(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case SET_SWIM_FORCE -> {
                    context.setSwimForce(buf.readFloat());
                    OpenBoatUtils.enabled = true;
                }
                case REMOVE_BLOCKS_SLIPPERINESS -> {
                    Arrays.stream(buf.readString().split(","))
                            .map(Identifier::of)
                            .forEach(context::unsetBlockSlipperiness);
                    OpenBoatUtils.enabled = true;
                }
                case CLEAR_SLIPPERINESS -> {
                    context.clearSlipperinessMap();
                    OpenBoatUtils.enabled = true;
                }
                case MODE_SERIES -> {
                    for (int i = 0; i < buf.readShort(); i++) {
                        Modes.setMode(Modes.values()[buf.readShort()]);
                    }
                    OpenBoatUtils.enabled = true;
                }
                case EXCLUSIVE_MODE_SERIES -> {
                    context.applyFrom(ISettingContext.VANILLA);
                    short amount = buf.readShort();
                    for (int i = 0; i < amount; i++) {
                        short mode = buf.readShort();
                        Modes.setMode(Modes.values()[mode]);
                    }
                    OpenBoatUtils.enabled = true;
                }
                case SET_PER_BLOCK -> {
                    short index = buf.readShort();
                    float value = buf.readFloat();

                    OpenBoatUtils.PerBlockSettingType[] settingTypes = OpenBoatUtils.PerBlockSettingType.values();

                    if (index >= settingTypes.length) return;
                    OpenBoatUtils.PerBlockSettingType setting = settingTypes[index];

                    Arrays.stream(buf.readString().split(","))
                            .map(Identifier::of)
                            .forEach(block -> context.setBlockSetting(block, setting, value));
                    OpenBoatUtils.enabled = true;
                }
                case SET_COLLISION_MODE -> {
                    short collisionMode = buf.readShort();

                    CollisionMode[] collisionModes = CollisionMode.values();

                    if (collisionMode >= collisionModes.length) return;

                    context.setCollisionMode(collisionModes[collisionMode]);
                    OpenBoatUtils.enabled = true;
                }
                case SET_STEP_WHILE_FALLING -> {
                    context.setStepWhileFalling(buf.readBoolean());
                    OpenBoatUtils.enabled = true;
                }
                case SET_INTERPOLATION_COMPAT -> {
                    OpenBoatUtils.setInterpolationCompat(buf.readBoolean());
                }
                case SET_COLLISION_RESOLUTION -> {
                    context.setCollisionResolution(buf.readByte());
                    OpenBoatUtils.enabled = true;
                }
                case ADD_COLLISION_ENTITYTYPE_FILTER -> {
                    Arrays.stream(buf.readString().split(","))
                            .map(EntityType::get)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(context::addToCollisionFilter);
                    OpenBoatUtils.enabled = true;
                }
                case CLEAR_COLLISION_ENTITYTYPE_FILTER -> {
                    context.clearCollisionFilter();
                    OpenBoatUtils.enabled = true;
                }
            }
        } catch (Exception E) {
            OpenBoatUtils.LOG.error("Error when handling clientbound openboatutils packet: ");
            for (StackTraceElement e : E.getStackTrace()){
                OpenBoatUtils.LOG.error(e.toString());
            }
        }
    }
}
