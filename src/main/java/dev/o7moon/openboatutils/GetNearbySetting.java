package dev.o7moon.openboatutils;

import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.spongepowered.asm.mixin.Unique;

public interface GetNearbySetting {
    @Unique
    float openboatutils$getAverageNearbySetting(ISettingContext context, AbstractBoatEntity instance, PerBlockSettingType setting);
}
