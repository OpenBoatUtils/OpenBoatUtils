package dev.o7moon.openboatutils.mixin;

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BoatEntity.class)
public interface BoatAccessor {
    @Accessor("pressingBack")
    boolean getPressingBack();
}
