package dev.o7moon.openboatutils;

import net.minecraft.entity.vehicle.AbstractBoatEntity ;

import java.util.HashMap;
import java.util.List;

// TODO impl the default context to see how well this api functions.
// it may need to become an abstract class

// this is unfinished, leaving it unused rn so i can release ten step interpolation asap

public interface ISettingContext {
    boolean enabled();
    boolean settingHasPerBlock(OpenBoatUtils.PerBlockSettingType setting);
    float getPerBlockForBlock(OpenBoatUtils.PerBlockSettingType setting, String blockid);
    float getNearbySetting(AbstractBoatEntity instance, OpenBoatUtils.PerBlockSettingType setting);
    float defaultPerBlock(OpenBoatUtils.PerBlockSettingType setting);
    HashMap<String, Float> getSlipperinessMap();
    void resetSettings();
    void setStepSize(float stepsize);
    void setBlocksSlipperiness(List<String> blocks, float slipperiness);
    void setAllBlocksSlipperiness(float slipperiness);
    void setBlockSlipperiness(String block, float slipperiness);
    float getBlockSlipperiness(String block);
    float getStepSize();
    void setFallDamage(boolean newValue);
    void setWaterElevation(boolean newValue);
    void setAirControl(boolean newValue);
    void setJumpForce(float newValue);
    void setGravityForce(double g);
    void setYawAcceleration(float accel);
    void setForwardsAcceleration(float accel);
    void setBackwardsAcceleration(float accel);
    void setTurningForwardsAcceleration(float accel);
    void setAllowAccelStacking(boolean value);
    void setUnderwaterControl(boolean value);
    void setSurfaceWaterControl(boolean value);
    void setCoyoteTime(int t);
    void setWaterJumping(boolean value);
    void setSwimForce(float value);
    void breakSlimePlease();
    void removeBlockSlipperiness(String block);
    void removeBlocksSlipperiness(List<String> blocks);
    void clearSlipperinessMap();
    float GetJumpForce(AbstractBoatEntity boat);
    float GetYawAccel(AbstractBoatEntity boat);
    float GetForwardAccel(AbstractBoatEntity boat);
    float GetBackwardAccel(AbstractBoatEntity boat);
    float GetTurnForwardAccel(AbstractBoatEntity boat);
    void setBlocksSetting(OpenBoatUtils.PerBlockSettingType setting, List<String> blocks, float value);
    void setBlockSetting(OpenBoatUtils.PerBlockSettingType setting, String block, float value);
    void setCollisionMode(CollisionMode mode);
    CollisionMode getCollisionMode();
    boolean canStepWhileFalling();
    void setCanStepWhileFalling(boolean canStepWhileFalling);
}
