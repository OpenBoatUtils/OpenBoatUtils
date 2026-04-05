package dev.o7moon.openboatutils.mixin;

import dev.o7moon.openboatutils.GetStepHeight;
import dev.o7moon.openboatutils.ISettingContext;
import dev.o7moon.openboatutils.OpenBoatUtils;
import dev.o7moon.openboatutils.PerBlockSettingType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private World world;

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
                World world = instance.getEntityWorld();

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

        instance.setVelocity(x, y, z);
    }

    @ModifyVariable(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("STORE"), ordinal = 3)
    private boolean hookStepHeightOnGroundCheck(boolean original) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return original;

        if ((Object) this instanceof BoatEntity) {
            if (context.hasStepWhileFalling()) {
                return true;
            }
        }

        return original;
    }
}
