package dev.o7moon.openboatutils;

import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Unique;

public interface GetNearbySetting {
    @Unique
    float openboatutils$getAverageNearbySetting(ISettingContext context, Boat instance, PerBlockSettingType setting);
}
