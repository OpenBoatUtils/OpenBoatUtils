package dev.o7moon.openboatutils.mixin;

import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBoatEntity.class)
public interface BoatAccessor {
    @Accessor("pressingBack")
    boolean getPressingBack();
}
