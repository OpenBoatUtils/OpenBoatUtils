package dev.o7moon.openboatutils.mixin;

import dev.o7moon.openboatutils.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private World world;

    @Shadow
    public abstract UUID getUuid();

    @Inject(method = "getStepHeight", at = @At("HEAD"), cancellable = true)
    public void getStepHeight(CallbackInfoReturnable<Float> cir) {
        if (this instanceof GetStepHeight step) {
            cir.setReturnValue(step.openboatutils$getStepHeight());
            cir.cancel();
        }
    }

    @Redirect(
            method = "move",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"
            )
    )
    private void hookWalltap(Entity instance, double x, double y, double z) {
        if ((Object) this instanceof BoatEntity) {
            ISettingContext context = OpenBoatUtils.instance.getActiveContext();

            if (context != null && (context.getWalltapMultiplier() > 0 ||
                    context.hasAnyBlocksWithSetting(PerBlockSettingType.WALLTAP_MULTIPLIER))) {

                Vec3d before = instance.getVelocity();
                float multiplier = context.getWalltapMultiplier();

                List<BlockPos> blockPositions = new ArrayList<>();

                if (context.hasAnyBlocksWithSetting(PerBlockSettingType.WALLTAP_MULTIPLIER)) {
                    Box box = instance.getBoundingBox();
                    Vec3d min = box.getMinPos();
                    Vec3d max = box.getMaxPos();

                    int minX = (int) Math.floor(min.x + 1e-5);
                    int minY = (int) Math.floor(min.y + 1e-5);
                    int minZ = (int) Math.floor(min.z + 1e-5);
                    int maxX = (int) Math.ceil(max.x - 1e-5);
                    int maxY = (int) Math.ceil(max.y - 1e-5);
                    int maxZ = (int) Math.ceil(max.z - 1e-5);

                    if (max.x % 1 == 0) {
                        for (int y1 = minY; y1 <= maxY; y1++) {
                            for (int z1 = minZ; z1 < maxZ; z1++) {
                                blockPositions.add(new BlockPos(maxX, y1, z1));
                            }
                        }
                    }

                    if (min.x % 1 == 0) {
                        for (int y1 = minY; y1 <= maxY; y1++) {
                            for (int z1 = minZ; z1 < maxZ; z1++) {
                                blockPositions.add(new BlockPos(minX - 1, y1, z1));
                            }
                        }
                    }

                    if (max.z % 1 == 0) {
                        for (int y1 = minY; y1 <= maxY; y1++) {
                            for (int x1 = minX; x1 < maxX; x1++) {
                                blockPositions.add(new BlockPos(x1, y1, maxZ));
                            }
                        }
                    }

                    if (min.z % 1 == 0) {
                        for (int y1 = minY; y1 <= maxY; y1++) {
                            for (int x1 = minX; x1 < maxX; x1++) {
                                blockPositions.add(new BlockPos(x1, y1, minZ - 1));
                            }
                        }
                    }
                }

                if (!blockPositions.isEmpty()) {
                    //? >= 1.21.9 {
                    /*World world = instance.getEntityWorld();
                     *///? } else {
                    World world = instance.getWorld();
                    //? }

                    int n = 0;
                    float multipliers = 0;

                    for (BlockPos pos : blockPositions) {
                        BlockState state = world.getBlockState(pos);

                        Float v = context.getBlockSetting(
                                Registries.BLOCK.getId(state.getBlock()),
                                PerBlockSettingType.WALLTAP_MULTIPLIER
                        );

                        if (v != null) {
                            n++;
                            multipliers += v;
                        }
                    }

                    if (n > 0) {
                        multiplier = multipliers / n;
                    }
                }

                if (multiplier > 0) {
                    if (x == 0) x = before.x * -multiplier;
                    if (z == 0) z = before.z * -multiplier;
                }
            }
        }

        instance.setVelocity(x, y, z);
    }

    @ModifyVariable(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("STORE"), ordinal = 3)
    private boolean hookStepHeightOnGroundCheck(boolean original) {

        if ((Object) this instanceof BoatEntity) {
            @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

            if (context == null) return original;

            if (context.hasStepWhileFalling()) {
                return true;
            }
        }

        return original;
    }

    @Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
    public void getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if ((Object) this instanceof BoatEntity) {
            @Nullable ISettingContext boatContext = OpenBoatUtils.instance.getEntityContext(this.getUuid());

            if (boatContext != null) {
                cir.setReturnValue(cir.getReturnValue().scaled(Math.abs(boatContext.getScale())));
            }

            @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

            if (context != null) {
                cir.setReturnValue(cir.getReturnValue().scaled(Math.abs(context.getScale())));
            }
        }
    }

    @Inject(
            method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
            at = @At(
                    value = "INVOKE",
                    //? >= 1.21.5 {
                    /*target = "Lnet/minecraft/util/math/Vec3d;subtract(DDD)Lnet/minecraft/util/math/Vec3d;",
                    *///? } else {
                    target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;",
                    //? }
                    shift = At.Shift.BEFORE
            )
    )
    private void hookStepUp(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
        if ((Object) this instanceof BoatEntity boat) {
            @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

            if (context != null) {
                float slipperiness = ((GetNearbySetting) boat).openboatutils$getAverageNearbySetting(context, boat, PerBlockSettingType.STEP_UP_SLIPPERINESS);

                if (slipperiness != 1) {
                    boat.setVelocity(boat.getVelocity().multiply(slipperiness));
                }
            }
        }
    }
}
