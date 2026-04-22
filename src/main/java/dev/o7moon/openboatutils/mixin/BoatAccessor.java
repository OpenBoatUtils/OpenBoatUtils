package dev.o7moon.openboatutils.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Boat.class)
public interface BoatAccessor {
    @Invoker("getStatus")
    Boat.Status getStatus();
}
