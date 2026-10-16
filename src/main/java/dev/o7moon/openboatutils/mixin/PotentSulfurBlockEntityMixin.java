package dev.o7moon.openboatutils.mixin;

import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PotentSulfurBlockEntity.class)
public class PotentSulfurBlockEntityMixin {

    // hopefully this never silently changes :pray:
    @ModifyConstant(
            method = "lambda$static$5",
            constant = @Constant(doubleValue = 0.30000001192092896)
    )
    private static double modifyBaseLaunchSpeed(double constant) {
        return 100;
    }

    @ModifyConstant(
            method = "lambda$static$5",
            constant = @Constant(doubleValue = 0.20000000298023224)
    )
    private static double modifyLaunchForce(double constant) {
        return 1;
    }
}
