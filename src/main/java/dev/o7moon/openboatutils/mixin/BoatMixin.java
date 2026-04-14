package dev.o7moon.openboatutils.mixin;

import dev.o7moon.openboatutils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
//? >= 1.21.5 {
/*import net.minecraft.entity.EntityType;
*///? }
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
//? >= 1.21.5 {
/*import net.minecraft.entity.PositionInterpolator;
*///? }
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
//? >= 1.21.5 {
/*import org.spongepowered.asm.mixin.Final;
*///? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? >= 1.21.5 {
/*import java.util.function.Supplier;
*///? }

@Mixin(BoatEntity.class)
public abstract class BoatMixin implements GetStepHeight {
    //? < 1.21.5 {
    @Shadow private float velocityDecay;
    //? }
    @Shadow private float nearbySlipperiness;
    @Shadow private double waterLevel;
    @Shadow private float yawVelocity;
    @Shadow private boolean pressingForward;
    @Shadow private boolean pressingBack;

    //? >=1.21.5 {
    /*@Shadow @Final
    private PositionInterpolator interpolator;
    @Unique private int openboatutils$defaultInterpolation;
    *///? }

    @Unique private int openboatutils$coyoteTimer;
    @Unique private int openboatutils$remaining_jumps;
    @Unique private boolean openboatutils$debounce = false;

    @Shadow protected abstract boolean checkBoatInWater();
    @Shadow protected abstract BoatEntity.Location checkLocation();

    @Shadow public abstract void remove(Entity.RemovalReason reason);

    @Unique private float openBoatUtils$lastScale;

    @Unique float openboatutils$stepHeight;
    @Unique public float openboatutils$getStepHeight() {
        return openboatutils$stepHeight;
    }
    @Unique public void openboatutils$setStepHeight(float f) {
        openboatutils$stepHeight = f;
    }

    @Unique public float openboatutils$getAverageNearbySetting(ISettingContext context, BoatEntity instance, PerBlockSettingType setting) {
        Box box = instance.getBoundingBox();
        Box box2 = new Box(box.minX, box.minY - 0.001, box.minZ, box.maxX, box.minY, box.maxZ);
        int i = MathHelper.floor(box2.minX) - 1;
        int j = MathHelper.ceil(box2.maxX) + 1;
        int k = MathHelper.floor(box2.minY) - 1;
        int l = MathHelper.ceil(box2.maxY) + 1;
        int m = MathHelper.floor(box2.minZ) - 1;
        int n = MathHelper.ceil(box2.maxZ) + 1;
        VoxelShape voxelShape = VoxelShapes.cuboid(box2);
        float f = 0.0f;
        int o = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int p = i; p < j; ++p) {
            for (int q = m; q < n; ++q) {
                int r = (p == i || p == j - 1 ? 1 : 0) + (q == m || q == n - 1 ? 1 : 0);
                if (r == 2) continue;
                for (int s = k; s < l; ++s) {
                    if (r > 0 && (s == k || s == l - 1)) continue;
                    mutable.set(p, s, q);

                    //? if >=1.21.9 {
                    /*World world = instance.getEntityWorld();
                    *///?} else {
                    World world = instance.getWorld();
                     //?}

                    BlockState blockState = world.getBlockState(mutable);

                    if (blockState.getBlock() instanceof LilyPadBlock || !VoxelShapes.matchesAnywhere(blockState.getCollisionShape(world, mutable).offset(p, s, q), voxelShape, BooleanBiFunction.AND)) continue;

                    Float value = context.getBlockSetting(Registries.BLOCK.getId(blockState.getBlock()), setting);

                    if (value == null) value = setting.fromContext(context);

                    f += value;
                    ++o;
                }
            }
        }


        Float air = context.getBlockSetting(Registries.BLOCK.getId(Blocks.AIR), setting);

        if (air == null) air = setting.fromContext(context);

        if (o == 0) return air;

        return f / (float) o;
    }

    @Unique public float openboatutils$getMaxNearbySetting(ISettingContext context, BoatEntity instance, PerBlockSettingType setting) {
        Box box = instance.getBoundingBox();
        Box box2 = new Box(box.minX, box.minY - 0.001, box.minZ, box.maxX, box.minY, box.maxZ);
        int i = MathHelper.floor(box2.minX) - 1;
        int j = MathHelper.ceil(box2.maxX) + 1;
        int k = MathHelper.floor(box2.minY) - 1;
        int l = MathHelper.ceil(box2.maxY) + 1;
        int m = MathHelper.floor(box2.minZ) - 1;
        int n = MathHelper.ceil(box2.maxZ) + 1;
        VoxelShape voxelShape = VoxelShapes.cuboid(box2);
        float f = 0.0f;
        int o = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int p = i; p < j; ++p) {
            for (int q = m; q < n; ++q) {
                int r = (p == i || p == j - 1 ? 1 : 0) + (q == m || q == n - 1 ? 1 : 0);
                if (r == 2) continue;
                for (int s = k; s < l; ++s) {
                    if (r > 0 && (s == k || s == l - 1)) continue;
                    mutable.set(p, s, q);

                    //? if >=1.21.9 {
                    /*World world = instance.getEntityWorld();
                     *///?} else {
                    World world = instance.getWorld();
                    //?}

                    BlockState blockState = world.getBlockState(mutable);

                    if (blockState.getBlock() instanceof LilyPadBlock || !VoxelShapes.matchesAnywhere(blockState.getCollisionShape(world, mutable).offset(p, s, q), voxelShape, BooleanBiFunction.AND)) continue;

                    Float value = context.getBlockSetting(Registries.BLOCK.getId(blockState.getBlock()), setting);

                    if (value == null) value = setting.fromContext(context);

                    f = Math.max(f, value);
                    ++o;
                }
            }
        }

        Float air = context.getBlockSetting(Registries.BLOCK.getId(Blocks.AIR), setting);

        if (air == null) air = setting.fromContext(context);

        if (o == 0) return air;

        return f;
    }

    @Unique
    void oncePerTick(BoatEntity instance, BoatEntity.Location loc, MinecraftClient minecraft) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return;

        if ((loc ==  BoatEntity.Location.UNDER_FLOWING_WATER || loc == BoatEntity.Location.UNDER_WATER) && minecraft.options.jumpKey.isPressed() && context.getSwimForce() != 0.0f) {
            Vec3d velocity = instance.getVelocity();
            instance.setVelocity(velocity.x, velocity.y + context.getSwimForce(), velocity.z);
        }

        if (loc == BoatEntity.Location.ON_LAND || (context.hasWaterJumping() && loc == BoatEntity.Location.IN_WATER)) {
            openboatutils$coyoteTimer = context.getCoyoteTime();
            openboatutils$remaining_jumps = context.getJumps();
            openboatutils$debounce = false;

            if (context.hasAnyBlocksWithSetting(PerBlockSettingType.COYOTE_TIME)) {
                openboatutils$coyoteTimer = Math.round(openboatutils$getMaxNearbySetting(context, instance, PerBlockSettingType.COYOTE_TIME));
            }

            if (context.hasAnyBlocksWithSetting(PerBlockSettingType.JUMPS)) {
                openboatutils$remaining_jumps = Math.round(openboatutils$getMaxNearbySetting(context, instance, PerBlockSettingType.JUMPS));
            }
        } else {
            openboatutils$coyoteTimer--;

            if (openboatutils$coyoteTimer == -1) {
                openboatutils$remaining_jumps--;
            }
        }

        float jumpForce = openboatutils$getAverageNearbySetting(context, instance, PerBlockSettingType.JUMP_FORCE);

        boolean jumping = minecraft.options.jumpKey.isPressed();

        if (!jumping) openboatutils$debounce = false;

        if (openboatutils$remaining_jumps > 0 && jumpForce > 0f && jumping && !openboatutils$debounce) {
            Vec3d velocity = instance.getVelocity();
            instance.setVelocity(velocity.x, jumpForce, velocity.z);

            openboatutils$coyoteTimer = -1;
            openboatutils$remaining_jumps--;
            openboatutils$debounce = true;
        }
    }

    //? >= 1.21.5 {
    /*@Inject(method = "<init>", at = @At("RETURN"))
    private void hookConstructor(EntityType<?> type, World world, Supplier<?> itemSupplier, CallbackInfo ci) {
        openboatutils$defaultInterpolation = ((PositionInterpolatorAccessor) interpolator).getLerpDuration();
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void hookPositionInterpolator(CallbackInfo ci) {
        if (OpenBoatUtils.instance.getInterpolationCompatibility()) {
            interpolator.setLerpDuration(10);
            return;
        }

        interpolator.setLerpDuration(openboatutils$defaultInterpolation);
    }
    *///? } else if >= 1.21.3 {
    /*@ModifyVariable(method = "updateTrackedPositionAndAngles", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    int interpolationStepsHook(int interpolationSteps) {
        if (!OpenBoatUtils.instance.getInterpolationCompatibility()) return interpolationSteps;
        return 10;
    }
    *///? }

    //? if >=1.21.3 {
    /*@Redirect(method = "getPaddleSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;checkLocation()Lnet/minecraft/entity/vehicle/BoatEntity$Location;"))
    *///?} else {
    @Redirect(method = "getPaddleSoundEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;checkLocation()Lnet/minecraft/entity/vehicle/BoatEntity$Location;"))
    //?}
    BoatEntity.Location paddleHook(BoatEntity instance) {
        return hookCheckLocation(instance, false);
    }

    @Redirect(method = {"tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;checkLocation()Lnet/minecraft/entity/vehicle/BoatEntity$Location;"))
    BoatEntity.Location tickHook(BoatEntity instance) {
        return hookCheckLocation(instance, true);
    }

    @Unique
    BoatEntity.Location hookCheckLocation(BoatEntity instance, boolean is_tick) {
        BoatMixin mixedInstance = (BoatMixin) (Object) instance;

        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        assert mixedInstance != null;

        mixedInstance.openboatutils$setStepHeight(0f);

        BoatEntity.Location loc = this.checkLocation();
        BoatEntity.Location original_loc = loc;

        if (context == null) return loc;

        MinecraftClient minecraft = MinecraftClient.getInstance();
        if (minecraft == null) return loc;
        PlayerEntity player = minecraft.player;
        if (player == null) return loc;
        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof BoatEntity boat)) return loc;

        if (!boat.equals(instance)) return loc;
        if (is_tick) oncePerTick(instance, loc, minecraft);

        mixedInstance.openboatutils$setStepHeight(context.getStepSize());

        if (loc == BoatEntity.Location.UNDER_WATER || loc == BoatEntity.Location.UNDER_FLOWING_WATER) {
            if (context.hasWaterElevation()) {
                instance.setPosition(instance.getX(), this.waterLevel += 1.0, instance.getZ());
                Vec3d velocity = instance.getVelocity();
                instance.setVelocity(velocity.x, 0f, velocity.z);// parity with old boatutils, but maybe in the future
                // there should be an implementation with different y velocities here.
                return BoatEntity.Location.IN_WATER;
            }
            return loc;
        }

        if (this.checkBoatInWater()) {
            if (context.hasWaterElevation()) {
                Vec3d velocity = instance.getVelocity();
                instance.setVelocity(velocity.x, 0.0, velocity.z);
            }
            loc = BoatEntity.Location.IN_WATER;
        }

        if (original_loc == BoatEntity.Location.IN_AIR && context.hasAirControl()) {
            Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(Blocks.AIR));

            if (slipperiness == null) slipperiness = context.getDefaultSlipperiness();

            this.nearbySlipperiness = slipperiness;
            loc = BoatEntity.Location.ON_LAND;
        }

        return loc;
    }

    @Redirect(method = "getNearbySlipperiness", at = @At(value="INVOKE",target="Lnet/minecraft/block/Block;getSlipperiness()F"))
    float getFriction(Block block) {

        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return block.getSlipperiness();

        Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(block));

        if (slipperiness != null) return slipperiness;

        return context.getDefaultSlipperiness();
    }

    @Inject(method = "collidesWith", at = @At("HEAD"), cancellable = true)
    void canCollideHook(Entity other, CallbackInfoReturnable<Boolean> ci) {

        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return;

        CollisionMode mode = context.getCollisionMode();

        // TODO: investigate this logic

        if (mode == CollisionMode.VANILLA) return;

        if ((mode == CollisionMode.NO_BOATS_OR_PLAYERS || mode == CollisionMode.NO_BOATS_OR_PLAYERS_PLUS_FILTER) &&
                (other instanceof BoatEntity || other instanceof PlayerEntity)) {
            ci.setReturnValue(false);
            ci.cancel();
            return;
        }
        if (mode == CollisionMode.NO_ENTITIES) {
            ci.setReturnValue(false);
            ci.cancel();
            return;
        }
        if ((mode == CollisionMode.ENTITYTYPE_FILTER || mode == CollisionMode.NO_BOATS_OR_PLAYERS_PLUS_FILTER) &&
                (context.isEntityTypeFiltered(other.getType()))) {
            ci.setReturnValue(false);
            ci.cancel();
            return;
        }
    }

    @Inject(method = "fall", at = @At("HEAD"), cancellable = true)
    void fallHook(CallbackInfo ci) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return;

        if (!context.hasFallDamage()) ci.cancel();
    }


    @Inject(method = "getGravity", at = @At("HEAD"), cancellable = true)
    public void onGetGravity(CallbackInfoReturnable<Double> cir) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return;

        cir.setReturnValue(-context.getGravityForce());
        cir.cancel();
    }

    @Redirect(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;yawVelocity:F", opcode = Opcodes.PUTFIELD))
    private void redirectYawVelocityIncrement(BoatEntity instance, float yawVelocity) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) {
            this.yawVelocity = yawVelocity;
            return;
        }
        float original_delta = yawVelocity - this.yawVelocity;
        // sign isn't needed here because the vanilla acceleration is exactly 1,
        // but I suppose this helps if mojang ever decides to change that value for some reason

        float yaw_accel = openboatutils$getAverageNearbySetting(context, instance, PerBlockSettingType.YAW_ACCEL);

        this.yawVelocity += MathHelper.sign(original_delta) * yaw_accel;
    }

    // a whole lotta modifyconstants because mojang put the acceleration values in literals
    @ModifyConstant(method = "updatePaddles", constant = @Constant(floatValue = 0.04f, ordinal = 0))
    private float forwardsAccel(float original) {
        ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return original;

        return openboatutils$getAverageNearbySetting(context, (BoatEntity) (Object) this, PerBlockSettingType.FORWARDS_ACCEL);
    }

    @ModifyConstant(method = "updatePaddles", constant = @Constant(floatValue = 0.005f, ordinal = 0))
    private float turnAccel(float original) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return original;

        return openboatutils$getAverageNearbySetting(context, (BoatEntity) (Object) this, PerBlockSettingType.TURN_FORWARDS_ACCEL);
    }

    @ModifyConstant(method = "updatePaddles", constant = @Constant(floatValue = 0.005f, ordinal = 1))
    private float backwardsAccel(float original) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) return original;

        return openboatutils$getAverageNearbySetting(context, (BoatEntity) (Object) this, PerBlockSettingType.BACKWARDS_ACCEL);
    }

    @Redirect(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingForward:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    private boolean pressingForwardHook(BoatEntity instance) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null || !context.hasAllowAccelStacking()) return this.pressingForward;

        return false;
    }

    @Redirect(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingBack:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    private boolean pressingBackHook(BoatEntity instance) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null || !context.hasAllowAccelStacking()) return this.pressingBack;

        return false;
    }

    //? >= 1.21.5 {
    /*// UNDER_FLOWING_WATER
    @ModifyConstant(method="updateVelocity", constant = @Constant(floatValue = 0.9F, ordinal = 1))
    private float velocityDecayHook1(float constant) {
        ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context != null && context.hasUnderwaterControl()) {
            Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(Blocks.WATER));;

            if (slipperiness != null) return slipperiness;

            return context.getDefaultSlipperiness();
        }

        return constant;
    }

    // UNDER_WATER
    @ModifyConstant(method="updateVelocity", constant = @Constant(floatValue = 0.45F))
    private float velocityDecayHook2(float constant) {
        ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context != null && context.hasUnderwaterControl()) {
            Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(Blocks.WATER));;

            if (slipperiness != null) return slipperiness;

            return context.getDefaultSlipperiness();
        }

        return constant;
    }

    // IN_WATER
    @ModifyConstant(method="updateVelocity", constant = @Constant(floatValue = 0.9F, ordinal = 0))
    private float velocityDecayHook3(float constant) {
        ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context != null && context.hasSurfaceWaterControl()) {
            Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(Blocks.WATER));;

            if (slipperiness != null) return slipperiness;

            return context.getDefaultSlipperiness();
        }

        return constant;
    }
    *///? } else {
    // UNDER_FLOWING_WATER velocity decay
    @Redirect(method="updateVelocity", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;velocityDecay:F", opcode = Opcodes.PUTFIELD, ordinal = 2))
    private void velocityDecayHook1(BoatEntity boat, float orig) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null || !context.hasUnderwaterControl()) {
            velocityDecay = orig;
            return;
        }

        Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(Blocks.WATER));

        velocityDecay = slipperiness != null
                ? slipperiness
                : context.getDefaultSlipperiness();
    }


    // UNDER_WATER velocity decay
    @Redirect(method="updateVelocity", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;velocityDecay:F", opcode = Opcodes.PUTFIELD, ordinal = 3))
    private void velocityDecayHook2(BoatEntity boat, float orig) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null || !context.hasUnderwaterControl()) {
            velocityDecay = orig;
            return;
        }

        Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(Blocks.WATER));

        velocityDecay = slipperiness != null
                ? slipperiness
                : context.getDefaultSlipperiness();
    }


    // IN_WATER velocity decay
    @Redirect(method="updateVelocity", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;velocityDecay:F", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void velocityDecayHook3(BoatEntity boat, float orig) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null || !context.hasSurfaceWaterControl()) {
            velocityDecay = orig;
            return;
        }

        Float slipperiness = context.getBlockSlipperiness(Registries.BLOCK.getId(Blocks.WATER));

        velocityDecay = slipperiness != null
                ? slipperiness
                : context.getDefaultSlipperiness();
    }
    //? }


    // Increase resolution for wall priority by running move() multiple times in smaller increments
    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
    private void moveHook(BoatEntity instance, MovementType movementType, Vec3d vec3d) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context == null) {
            instance.move(movementType, vec3d);
            return;
        }

        int resolution = context.getCollisionResolution();

        if (resolution < 1 || resolution > 50) {
            instance.move(movementType, vec3d);
            return;
        }

        Vec3d subMoveVel = instance.getVelocity().multiply(1d / resolution);
        for(int i = 0; i < resolution; i++) {
            instance.move(movementType, subMoveVel);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        float currentScale = 1f;

        if (context != null) {
            currentScale = context.getScale();
        }

        if (currentScale != openBoatUtils$lastScale) {
            openBoatUtils$lastScale = currentScale;
            ((BoatEntity) (Object) this).calculateDimensions();
        }
    }
}
