package dev.o7moon.openboatutils;

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Unique;

public interface GetNearbySetting {
    @Unique
    float openboatutils$getAverageNearbySetting(ISettingContext context, BoatEntity instance, PerBlockSettingType setting);
}
