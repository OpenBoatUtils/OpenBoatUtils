package dev.o7moon.openboatutils;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;

public enum Modes {
    BROKEN_SLIME_RALLY,//0
    BROKEN_SLIME_RALLY_BLUE,//1
    BROKEN_SLIME_BA_NOFD,//2
    BROKEN_SLIME_PARKOUR,//3
    BROKEN_SLIME_BA_BLUE_NOFD,//4
    BROKEN_SLIME_PARKOUR_BLUE,//5
    BROKEN_SLIME_BA,//6
    BROKEN_SLIME_BA_BLUE,//7
    RALLY,//8
    RALLY_BLUE,//9
    BA_NOFD,//10
    PARKOUR,//11
    BA_BLUE_NOFD,//12
    PARKOUR_BLUE,//13
    BA,//14
    BA_BLUE,//15
    JUMP_BLOCKS,//16
    BOOSTER_BLOCKS,//17
    DEFAULT_ICE,//18
    DEFAULT_NINE_EIGHT_FIVE,//19
    NOCOL_BOATS_AND_PLAYERS,//20
    NOCOL_ALL_ENTITIES,//21
    BA_JANKLESS,//22
    BA_BLUE_JANKLESS,//23
    DEFAULT_BLUE_ICE
    ;

    public static void setMode(Modes mode) {
        SettingContext context = OpenBoatUtils.instance;

        // TODO: statically init modes / redo this
        
        switch (mode){
            case RALLY:
                context.setDefaultSlipperiness(0.98f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setStepSize(1.25f);
                return;
            case RALLY_BLUE:
                context.setDefaultSlipperiness(0.989f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setStepSize(1.25f);
                return;
            case BA_NOFD:
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.98f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                return;
            case PARKOUR:
                context.setDefaultSlipperiness(0.98f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setJumpForce(0.36f);
                context.setStepSize(0.5f);
                return;
            case BA_BLUE_NOFD:
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.989f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                return;
            case PARKOUR_BLUE:
                context.setDefaultSlipperiness(0.989f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setJumpForce(0.36f);
                context.setStepSize(0.5f);
                return;
            case BA:
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.98f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                return;
            case BA_BLUE:
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.989f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                return;
            case BROKEN_SLIME_RALLY:
                context.setDefaultSlipperiness(0.98f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setStepSize(1.25f);
                context.breakSlimePlease();
                return;
            case BROKEN_SLIME_RALLY_BLUE:
                context.setDefaultSlipperiness(0.989f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setStepSize(1.25f);
                context.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA_NOFD:
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.98f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                context.breakSlimePlease();
                return;
            case BROKEN_SLIME_PARKOUR:
                context.setDefaultSlipperiness(0.98f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setJumpForce(0.36f);
                context.setStepSize(0.5f);
                context.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA_BLUE_NOFD:
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.989f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                context.breakSlimePlease();
                return;
            case BROKEN_SLIME_PARKOUR_BLUE:
                context.setDefaultSlipperiness(0.989f);
                context.setFallDamage(false);
                context.setAirControl(true);
                context.setJumpForce(0.36f);
                context.setStepSize(0.5f);
                context.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA:
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.98f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                context.breakSlimePlease();
                return;
            case BROKEN_SLIME_BA_BLUE:
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.989f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                context.breakSlimePlease();
                return;
            case JUMP_BLOCKS:
                context.setBlockSetting(Registries.BLOCK.getId(Blocks.ORANGE_CONCRETE), OpenBoatUtils.PerBlockSettingType.JUMP_FORCE, 0.36f);
                context.setBlockSetting(Registries.BLOCK.getId(Blocks.BLACK_CONCRETE), OpenBoatUtils.PerBlockSettingType.JUMP_FORCE, 0.0f);
                context.setBlockSetting(Registries.BLOCK.getId(Blocks.GREEN_CONCRETE), OpenBoatUtils.PerBlockSettingType.JUMP_FORCE, 0.5f);
                context.setBlockSetting(Registries.BLOCK.getId(Blocks.YELLOW_CONCRETE), OpenBoatUtils.PerBlockSettingType.JUMP_FORCE, 0.18f);
                return;
            case BOOSTER_BLOCKS:
                context.setBlockSetting(Registries.BLOCK.getId(Blocks.MAGENTA_GLAZED_TERRACOTTA), OpenBoatUtils.PerBlockSettingType.FORWARDS_ACCEL, 0.08f); // double accel
                context.setBlockSetting(Registries.BLOCK.getId(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA), OpenBoatUtils.PerBlockSettingType.YAW_ACCEL, 0.08f); // double yaw accel
                return;
            case DEFAULT_ICE:
                context.setDefaultSlipperiness(0.98f);
                return;
            case DEFAULT_NINE_EIGHT_FIVE:
                context.setDefaultSlipperiness(0.985f);
                return;
            case NOCOL_BOATS_AND_PLAYERS:
                context.setCollisionMode(CollisionMode.NO_BOATS_OR_PLAYERS);
                return;
            case NOCOL_ALL_ENTITIES:
                context.setCollisionMode(CollisionMode.NO_ENTITIES);
                return;
            case BA_JANKLESS:
                context.setStepWhileFalling(true);
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.98f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                return;
            case BA_BLUE_JANKLESS:
                context.setStepWhileFalling(true);
                context.setAirControl(true);
                context.setBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR), 0.989f);
                context.setStepSize(1.25f);
                context.setWaterElevation(true);
                return;
            case DEFAULT_BLUE_ICE:
                context.setDefaultSlipperiness(0.989f);
                return;
        }
    }
}
