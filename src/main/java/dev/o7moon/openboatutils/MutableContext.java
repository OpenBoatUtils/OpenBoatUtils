package dev.o7moon.openboatutils;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class MutableContext implements ISettingContext {

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
    private float walltapMultiplier;
    private int jumps;
    private float scale;

    private final Map<Identifier, Float> blockSlipperiness = new HashMap<>(ISettingContext.getVanillaSlipperinessMap());
    private final Map<PerBlockSettingType, Map<Identifier, Float>> blockSettings = new HashMap<>();
    private final Set<EntityType<?>> collisionFilteredEntities = new HashSet<>();

    private Set<Identifier> blocksWithSettings = new HashSet<>();
    private Set<PerBlockSettingType> settingsInUse = new HashSet<>();

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
    @Override public boolean isEntityTypeFiltered(EntityType<?> type) { return collisionFilteredEntities.contains(type); }
    @Override public @Nullable Float getBlockSetting(Identifier id, PerBlockSettingType type) {
        if (!blocksWithSettings.contains(id)) return null;

        return blockSettings
                .computeIfAbsent(type, unused -> new HashMap<>())
                .get(id);
    }
    @Override public int getCollisionResolution() { return collisionResolution; }
    @Override public float getWalltapMultiplier() { return walltapMultiplier; }
    @Override public int getJumps() { return jumps; }
    @Override public float getScale() { return scale; }

    @Override
    public Set<Identifier> getBlocksWithSettings() { return blocksWithSettings; }

    @Override
    public boolean hasAnyBlocksWithSetting(PerBlockSettingType type) { return settingsInUse.contains(type); }

    public MutableContext setFallDamage(boolean v) { this.hasFallDamage = v; return this; }
    public MutableContext setWaterElevation(boolean v) { this.hasWaterElevation = v; return this; }
    public MutableContext setAirControl(boolean v) { this.hasAirControl = v; return this; }
    public MutableContext setDefaultSlipperiness(float v) { this.defaultSlipperiness = v; return this; }
    public MutableContext setJumpForce(float v) { this.jumpForce = v; return this; }
    public MutableContext setStepSize(float v) { this.stepSize = v; return this; }
    public MutableContext setGravityForce(double v) { this.gravityForce = v; return this; }
    public MutableContext setYawAccel(float v) { this.yawAccel = v; return this; }
    public MutableContext setForwardAccel(float v) { this.forwardAccel = v; return this; }
    public MutableContext setBackwardAccel(float v) { this.backwardAccel = v; return this; }
    public MutableContext setTurnForwardAccel(float v) { this.turnForwardAccel = v; return this; }
    public MutableContext setAllowAccelStacking(boolean v) { this.allowAccelStacking = v; return this; }
    public MutableContext setUnderwaterControl(boolean v) { this.underwaterControl = v; return this; }
    public MutableContext setSurfaceWaterControl(boolean v) { this.surfaceWaterControl = v; return this; }
    public MutableContext setCoyoteTime(int v) { this.coyoteTime = v; return this; }
    public MutableContext setWaterJumping(boolean v) { this.waterJumping = v; return this; }
    public MutableContext setSwimForce(float v) { this.swimForce = v; return this; }
    public MutableContext setCollisionMode(CollisionMode v) { this.collisionMode = v; return this; }
    public MutableContext setStepWhileFalling(boolean v) { this.stepWhileFalling = v; return this; }
    public MutableContext setCollisionResolution(int v) { this.collisionResolution = v; return this; }
    public MutableContext setWalltapMultiplier(float v) { this.walltapMultiplier = v; return this; }
    public MutableContext setJumps(int jumps) { this.jumps = jumps; return this; }
    public MutableContext setScale(float scale) { this.scale = scale; return this; }

    public MutableContext addToCollisionFilter(EntityType<?> type) {
        this.collisionFilteredEntities.add(type);
        return this;
    }
    public MutableContext clearCollisionFilter() {
        this.collisionFilteredEntities.clear();
        return this;
    }

    public MutableContext setBlockSetting(Identifier id, PerBlockSettingType type, float value) {
        blocksWithSettings.add(id);
        settingsInUse.add(type);
        blockSettings
                .computeIfAbsent(type, unused -> new HashMap<>())
                .put(id, value);
        return this;
    }

    public MutableContext breakSlimePlease() {
        this.blockSlipperiness.remove(Registries.BLOCK.getId(Blocks.SLIME_BLOCK));
        return this;
    }
    public MutableContext unsetBlockSlipperiness(Identifier id) {
        blockSlipperiness.remove(id);
        return this;
    }
    public MutableContext setBlockSlipperiness(Identifier id, float slipperiness) {
        blockSlipperiness.put(id, slipperiness);
        return this;
    }
    public MutableContext clearSlipperinessMap() {
        blockSlipperiness.clear();
        return this;
    }

    public MutableContext applyFrom(ISettingContext other) {
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
        this.walltapMultiplier = other.getWalltapMultiplier();
        this.jumps = other.getJumps();
        this.scale = other.getScale();

        this.blocksWithSettings = new HashSet<>(other.getBlocksWithSettings());
        this.settingsInUse = new HashSet<>(Arrays.stream(PerBlockSettingType.values()).filter(other::hasAnyBlocksWithSetting).toList());

        this.blockSlipperiness.clear();
        Registries.BLOCK.stream()
                .map(Registries.BLOCK::getId).forEach(identifier -> {
            Float slipperiness = other.getBlockSlipperiness(identifier);

            if (slipperiness == null) return;

            this.blockSlipperiness.put(identifier, slipperiness);
        });

        this.blockSettings.clear();
        blocksWithSettings.forEach(identifier -> {
            for (PerBlockSettingType type : PerBlockSettingType.values()) {
                Float setting = other.getBlockSetting(identifier, type);

                if (setting == null) continue;

                blockSettings
                        .computeIfAbsent(type, unused -> new HashMap<>())
                        .put(identifier, setting);
            }
        });

        this.collisionFilteredEntities.clear();
        this.collisionFilteredEntities.addAll(Registries.ENTITY_TYPE.stream().filter(other::isEntityTypeFiltered).toList());

        return this;
    }


}

