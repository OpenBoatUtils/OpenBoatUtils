package dev.o7moon.openboatutils;

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