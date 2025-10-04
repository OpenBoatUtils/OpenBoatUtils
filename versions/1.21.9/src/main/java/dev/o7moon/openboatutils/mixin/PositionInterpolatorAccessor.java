package dev.o7moon.openboatutils.mixin;

import net.minecraft.entity.PositionInterpolator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PositionInterpolator.class)
public interface PositionInterpolatorAccessor {
    @Accessor("lerpDuration")
    int getLerpDuration();
}
