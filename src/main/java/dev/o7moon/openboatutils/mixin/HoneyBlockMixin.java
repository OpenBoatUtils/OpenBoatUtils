package dev.o7moon.openboatutils.mixin;


import net.minecraft.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;
//? > 1.21.1 {
/*import dev.o7moon.openboatutils.ISettingContext;
import dev.o7moon.openboatutils.OpenBoatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///? }

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {
    //? > 1.21.1 {
    /*@Shadow
    private static double method_65067(double par1) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    private static double method_65068(double par1) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Redirect(method = "updateSlidingVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HoneyBlock;method_65067(D)D"))
    private double restoreVelocityThreshold(double v) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context != null && context.hasHoneyCompatibility()) {
            return v;
        }

        return method_65067(v);
    }

    @Redirect(method = "updateSlidingVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HoneyBlock;method_65068(D)D"))
    private double restoreVelocitySet(double v) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context != null && context.hasHoneyCompatibility()) {
            return v;
        }

        return method_65068(v);
    }

    @Inject(method = "isSliding", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HoneyBlock;method_65067(D)D"), cancellable = true)
    private void restoreSlidingThreshold(BlockPos pos, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();
        if (context != null && context.hasHoneyCompatibility()) {
            if (entity.getVelocity().y >= -0.08) {
                cir.setReturnValue(false);
            }
        }
    }

    @Redirect(method = "isSliding", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HoneyBlock;method_65067(D)D"))
    private double restoreSlidingVelocityCheck(double v) {
        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();
        if (context != null && context.hasHoneyCompatibility()) {
            return v;
        }
        return method_65067(v);
    }
    *///? }
}
