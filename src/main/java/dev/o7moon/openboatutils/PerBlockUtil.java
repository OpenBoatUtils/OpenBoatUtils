package dev.o7moon.openboatutils;

import io.github.openboatutils.protocol.channels.OBUSettingsPacket;

public interface PerBlockUtil {
    static float fromContext(OBUSettingsPacket.PerBlockSetting perBlockSetting, ISettingContext context) {
        return switch (perBlockSetting) {
            case JUMP_FORCE -> context.getJumpForce();
            case FORWARD_ACCEL -> context.getForwardAccel();
            case BACKWARD_ACCEL -> context.getBackwardAccel();
            case YAW_ACCEL -> context.getYawAccel();
            case TURN_FORWARDS_ACCEL -> context.getTurnForwardAccel();
            case WALLTAP_MULTIPLIER -> context.getWalltapMultiplier();
            case JUMPS -> context.getJumps();
            case COYOTE_TIME -> context.getCoyoteTime();
            case STEP_UP_SLIPPERINESS -> context.getStepUpSlipperiness();
            case LATERAL_SLIPPERINESS -> context.getLateralSlipperiness();
            case BRAKE_SLIPPERINESS -> context.getBrakeSlipperiness();
            case MAX_SPEED -> context.getMaxSpeed();
            case MAX_SPEED_RESISTANCE -> context.getMaxSpeedResistance();
        };
    }
}
