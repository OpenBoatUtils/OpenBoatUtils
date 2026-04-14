package dev.o7moon.openboatutils;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface ISettingContext {

    ISettingContext VANILLA = getVanilla();
    Map<Identifier, Float> VANILLA_SLIPPERINESS = getVanillaSlipperinessMap();

    void switchTo();

    boolean hasFallDamage();
    boolean hasWaterElevation();
    boolean hasAirControl();
    float getDefaultSlipperiness();
    float getJumpForce();
    float getStepSize();
    double getGravityForce();
    float getYawAccel();
    float getForwardAccel();
    float getBackwardAccel();
    float getTurnForwardAccel();
    boolean hasAllowAccelStacking();
    boolean hasUnderwaterControl();
    boolean hasSurfaceWaterControl();
    int getCoyoteTime();
    boolean hasWaterJumping();
    float getSwimForce();
    CollisionMode getCollisionMode();
    boolean hasStepWhileFalling();
    @Nullable Float getBlockSlipperiness(Identifier id);
    boolean isEntityTypeFiltered(EntityType<?> type);
    @Nullable Float getBlockSetting(Identifier id, PerBlockSettingType type);
    int getCollisionResolution();
    float getWalltapMultiplier();
    int getJumps();
    float getScale();

    Set<Identifier> getBlocksWithSettings();
    boolean hasAnyBlocksWithSetting(PerBlockSettingType type);

    static Map<Identifier, Float> getVanillaSlipperinessMap() {
        Map<Identifier, Float> map = new HashMap<>();

        for (Block b : Registries.BLOCK.stream().toList()) {
            if (b.getSlipperiness() != 0.6f){
                map.put(Registries.BLOCK.getId(b), b.getSlipperiness());
            }
        }

        return map;
    }

    private static ISettingContext getVanilla() {
        return new ISettingContext() {
            @Override
            public void switchTo() {}

            @Override public boolean hasFallDamage() { return true; }
            @Override public boolean hasWaterElevation() { return false; }
            @Override public boolean hasAirControl() { return false; }
            @Override public float getDefaultSlipperiness() { return 0.6f; }
            @Override public float getJumpForce() { return 0; }
            @Override public float getStepSize() { return 0; }
            @Override public double getGravityForce() { return -0.03999999910593033; }
            @Override public float getYawAccel() { return 1f; }
            @Override public float getForwardAccel() { return 0.04f; }
            @Override public float getBackwardAccel() { return 0.005f; }
            @Override public float getTurnForwardAccel() { return 0.005f; }
            @Override public boolean hasAllowAccelStacking() { return false; }
            @Override public boolean hasUnderwaterControl() { return false; }
            @Override public boolean hasSurfaceWaterControl() { return false; }
            @Override public int getCoyoteTime() { return 0; }
            @Override public boolean hasWaterJumping() { return false; }
            @Override public float getSwimForce() { return 0; }
            @Override public CollisionMode getCollisionMode() { return CollisionMode.VANILLA; }
            @Override public boolean hasStepWhileFalling() { return false; }
            @Override public @Nullable Float getBlockSlipperiness(Identifier id) { return VANILLA_SLIPPERINESS.get(id); }
            @Override public boolean isEntityTypeFiltered(EntityType<?> type) { return false; }
            @Override public @Nullable Float getBlockSetting(Identifier id, PerBlockSettingType type) { return null; }
            @Override public int getCollisionResolution() { return 1; }
            @Override public float getWalltapMultiplier() { return 0; }
            @Override public int getJumps() { return 1; }
            @Override public float getScale() { return 1; }

            @Override public Set<Identifier> getBlocksWithSettings() { return Set.of(); }

            @Override
            public boolean hasAnyBlocksWithSetting(PerBlockSettingType type) { return false; }
        };
    }
}