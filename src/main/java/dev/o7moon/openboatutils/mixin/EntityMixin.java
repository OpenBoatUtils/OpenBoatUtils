package dev.o7moon.openboatutils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.o7moon.openboatutils.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Shadow
    private static Vec3d adjustMovementForCollisions(Vec3d movement, Box entityBoundingBox, List<VoxelShape> collisions) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static float[] collectStepHeights(Box collisionBox, List<VoxelShape> collisions, float f, float stepHeight) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static List<VoxelShape> findCollisionsForMovement(@Nullable Entity entity, World world, List<VoxelShape> regularCollisions, Box movingEntityBoundingBox) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

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
        if ((Object) this instanceof AbstractBoatEntity) {
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

        if ((Object) this instanceof AbstractBoatEntity) {
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
        if ((Object) this instanceof AbstractBoatEntity) {
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
        if ((Object) this instanceof AbstractBoatEntity boat) {
            @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

            if (context != null) {
                float slipperiness = ((GetNearbySetting) boat).openboatutils$getAverageNearbySetting(context, boat, PerBlockSettingType.STEP_UP_SLIPPERINESS);

                if (slipperiness != 1) {
                    boat.setVelocity(boat.getVelocity().multiply(slipperiness));
                }
            }
        }
    }

    // Previously based on https://github.com/Moulberry/MC276641Fix/blob/master/src/main/java/com/moulberry/mc276641fix/mixin/MixinEntity.java
    // Modified to re-evaluate the candidates after each step so enable stepping past the step size if it is possible to step multiple times
    @Inject(
            method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;collectStepHeights(Lnet/minecraft/util/math/Box;Ljava/util/List;FF)[F",
                    shift = At.Shift.BY,
                    by = 2
            ),
            cancellable = true
    )
    private void fixMultipleStep(
            Vec3d velocity,
            CallbackInfoReturnable<Vec3d> cir,
            @Local(ordinal = 1) List<VoxelShape> colliders,
            @Local(ordinal = 0) Box aABB,
            @Local(ordinal = 1) Box aABB2,
            @Local(ordinal = 1) Vec3d vec3d
    ) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();
        Entity entity = (Entity) (Object) this;
        if (context != null && entity instanceof AbstractBoatEntity boat && context.hasMultiStepping()) {
            Vec3d result = openboatutils$attemptStep(boat, velocity, aABB2, colliders, vec3d, 0, 100);
            double d = aABB.minY - aABB2.minY;

            double correctedY = Math.abs(result.y - vec3d.y) > 1.0E-7
                    ? result.y - d
                    : vec3d.y;

            cir.setReturnValue(new Vec3d(result.x, correctedY, result.z));
        }
    }

    @Unique
    private Vec3d openboatutils$attemptStep(AbstractBoatEntity boat, Vec3d velocity, Box box, List<VoxelShape> colliders, Vec3d fallback, int depth, int maxDepth) {
        if (depth >= maxDepth) return fallback;

        float f = (float) fallback.y;
        float[] heights = collectStepHeights(box, colliders, boat.getStepHeight(), f);

        Vec3d best = fallback;

        for (float height : heights) {
            Vec3d candidate = adjustMovementForCollisions(
                    new Vec3d(velocity.x, height, velocity.z), box, colliders
            );
            if (candidate.horizontalLengthSquared() > best.horizontalLengthSquared()) {
                best = candidate;
            }
        }

        if (best == fallback) return fallback;

        Vec3d remaining = new Vec3d(
                velocity.x - best.x,
                velocity.y,
                velocity.z - best.z
        );

        if (remaining.horizontalLengthSquared() > 1.0E-7) {
            Box movedBox = box.offset(best.x, best.y, best.z);
            Box stretched = movedBox.stretch(remaining);

            List<VoxelShape> newColliders = findCollisionsForMovement(
                    boat,
                    this.world,
                    colliders,
                    stretched
            );

            Vec3d nextFallback = adjustMovementForCollisions(remaining, movedBox, newColliders);

            boolean blockedX = remaining.x != nextFallback.x;
            boolean blockedZ = remaining.z != nextFallback.z;

            if (blockedX || blockedZ) {
                Vec3d next = openboatutils$attemptStep(boat, remaining, movedBox, newColliders, nextFallback, depth + 1, maxDepth);
                best = best.add(next);
            } else {
                best = best.add(nextFallback);
            }
        }

        return best;
    }
}
