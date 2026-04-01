package dev.o7moon.openboatutils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SettingContext implements ISettingContext {

    private boolean hasFallDamage;
    private boolean hasWaterElevation;
    private boolean hasAirControl;
    private float defaultSlipperiness;
    private float jumpForce;
    private float stepSize;
    private double gravityForce;
    private float yawAccel;
    private float forwardAccel;
    private float backwardAccel;
    private float turnForwardAccel;
    private boolean allowAccelStacking;
    private boolean underwaterControl;
    private boolean surfaceWaterControl;
    private int coyoteTime;
    private boolean waterJumping;
    private float swimForce;
    private CollisionMode collisionMode;
    private boolean stepWhileFalling;
    private int collisionResolution;

    private final Map<Identifier, Float> blockSlipperiness = new HashMap<>(ISettingContext.getVanillaSlipperinessMap());
    private final Map<OpenBoatUtils.PerBlockSettingType, Map<Identifier, Float>> perBlockSettings = new HashMap<>();
    private final Set<EntityType<?>> collisionFilteredEntities = new HashSet<>();

    @Override public boolean hasFallDamage() { return hasFallDamage; }
    @Override public boolean hasWaterElevation() { return hasWaterElevation; }
    @Override public boolean hasAirControl() { return hasAirControl; }
    @Override public float getDefaultSlipperiness() { return defaultSlipperiness; }
    @Override public float getJumpForce() { return jumpForce; }
    @Override public float getStepSize() { return stepSize; }
    @Override public double getGravityForce() { return gravityForce; }
    @Override public float getYawAccel() { return yawAccel; }
    @Override public float getForwardAccel() { return forwardAccel; }
    @Override public float getBackwardAccel() { return backwardAccel; }
    @Override public float getTurnForwardAccel() { return turnForwardAccel; }
    @Override public boolean hasAllowAccelStacking() { return allowAccelStacking; }
    @Override public boolean hasUnderwaterControl() { return underwaterControl; }
    @Override public boolean hasSurfaceWaterControl() { return surfaceWaterControl; }
    @Override public int getCoyoteTime() { return coyoteTime; }
    @Override public boolean hasWaterJumping() { return waterJumping; }
    @Override public float getSwimForce() { return swimForce; }
    @Override public CollisionMode getCollisionMode() { return collisionMode; }
    @Override public boolean hasStepWhileFalling() { return stepWhileFalling; }
    @Override public @Nullable Float getBlockSlipperiness(Identifier id) {
        return blockSlipperiness.get(id);
    }
    @Override public boolean isEntityTypeFiltered(EntityType<?> type) { return false; }
    @Override public @Nullable Float getBlockSetting(Identifier id, OpenBoatUtils.PerBlockSettingType type) {
        return perBlockSettings
                .computeIfAbsent(type, unused -> new HashMap<>())
                .get(id);
    }
    @Override public int getCollisionResolution() { return collisionResolution; }

    public SettingContext setFallDamage(boolean v) { this.hasFallDamage = v; return this; }
    public SettingContext setWaterElevation(boolean v) { this.hasWaterElevation = v; return this; }
    public SettingContext setAirControl(boolean v) { this.hasAirControl = v; return this; }
    public SettingContext setDefaultSlipperiness(float v) { this.defaultSlipperiness = v; return this; }
    public SettingContext setJumpForce(float v) { this.jumpForce = v; return this; }
    public SettingContext setStepSize(float v) { this.stepSize = v; return this; }
    public SettingContext setGravityForce(double v) { this.gravityForce = v; return this; }
    public SettingContext setYawAccel(float v) { this.yawAccel = v; return this; }
    public SettingContext setForwardAccel(float v) { this.forwardAccel = v; return this; }
    public SettingContext setBackwardAccel(float v) { this.backwardAccel = v; return this; }
    public SettingContext setTurnForwardAccel(float v) { this.turnForwardAccel = v; return this; }
    public SettingContext setAllowAccelStacking(boolean v) { this.allowAccelStacking = v; return this; }
    public SettingContext setUnderwaterControl(boolean v) { this.underwaterControl = v; return this; }
    public SettingContext setSurfaceWaterControl(boolean v) { this.surfaceWaterControl = v; return this; }
    public SettingContext setCoyoteTime(int v) { this.coyoteTime = v; return this; }
    public SettingContext setWaterJumping(boolean v) { this.waterJumping = v; return this; }
    public SettingContext setSwimForce(float v) { this.swimForce = v; return this; }
    public SettingContext setCollisionMode(CollisionMode v) { this.collisionMode = v; return this; }
    public SettingContext setStepWhileFalling(boolean v) { this.stepWhileFalling = v; return this; }
    public SettingContext setCollisionResolution(int v) { this.collisionResolution = v; return this; }

    public SettingContext addToCollisionFilter(EntityType<?> type) {
        this.collisionFilteredEntities.add(type);
        return this;
    }
    public SettingContext removeFromCollisionFilter(EntityType<?> type) {
        this.collisionFilteredEntities.remove(type);
        return this;
    }
    public SettingContext clearCollisionFilter() {
        this.collisionFilteredEntities.clear();
        return this;
    }

    public SettingContext setBlockSetting(Identifier id, OpenBoatUtils.PerBlockSettingType type, float value) {
        perBlockSettings
                .computeIfAbsent(type, unused -> new HashMap<>())
                .put(id, value);
        return this;
    }

    public SettingContext breakSlimePlease() {
        this.blockSlipperiness.remove(Registries.BLOCK.getId(Blocks.SLIME_BLOCK));
        return this;
    }
    public SettingContext unsetBlockSlipperiness(Identifier id) {
        blockSlipperiness.remove(id);
        return this;
    }
    public SettingContext setBlockSlipperiness(Identifier id, float slipperiness) {
        blockSlipperiness.put(id, slipperiness);
        return this;
    }
    public SettingContext clearSlipperinessMap() {
        blockSlipperiness.clear();
        return this;
    }

    public SettingContext applyFrom(ISettingContext other) {
        this.hasFallDamage = other.hasFallDamage();
        this.hasWaterElevation = other.hasWaterElevation();
        this.hasAirControl = other.hasAirControl();
        this.defaultSlipperiness = other.getDefaultSlipperiness();
        this.jumpForce = other.getJumpForce();
        this.stepSize = other.getStepSize();
        this.gravityForce = other.getGravityForce();
        this.yawAccel = other.getYawAccel();
        this.forwardAccel = other.getForwardAccel();
        this.backwardAccel = other.getBackwardAccel();
        this.turnForwardAccel = other.getTurnForwardAccel();
        this.allowAccelStacking = other.hasAllowAccelStacking();
        this.underwaterControl = other.hasUnderwaterControl();
        this.surfaceWaterControl = other.hasSurfaceWaterControl();
        this.coyoteTime = other.getCoyoteTime();
        this.waterJumping = other.hasWaterJumping();
        this.swimForce = other.getSwimForce();
        this.collisionMode = other.getCollisionMode();
        this.stepWhileFalling = other.hasStepWhileFalling();
        this.collisionResolution = other.getCollisionResolution();

        this.blockSlipperiness.clear();

        for (Block b : Registries.BLOCK.stream().toList()) {
            Identifier id = Registries.BLOCK.getId(b);

            this.blockSlipperiness.put(id, other.getBlockSlipperiness(id));
        }

        this.collisionFilteredEntities.clear();
        this.collisionFilteredEntities.addAll(Registries.ENTITY_TYPE.stream().toList());

        return this;
    }

    public enum PerBlockSettingType {
        JUMP_FORCE,
        FORWARDS_ACCEL,
        BACKWARDS_ACCEL,
        YAW_ACCEL,
        TURN_FORWARDS_ACCEL;

        public float fromContext(ISettingContext context) {
            return switch (this) {
                case JUMP_FORCE -> context.getJumpForce();
                case FORWARDS_ACCEL -> context.getForwardAccel();
                case BACKWARDS_ACCEL -> context.getBackwardAccel();
                case YAW_ACCEL -> context.getYawAccel();
                case TURN_FORWARDS_ACCEL -> context.getTurnForwardAccel();
            };
        }
    }
}

