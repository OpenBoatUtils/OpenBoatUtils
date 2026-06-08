package dev.o7moon.openboatutils.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameOptions.class)
public interface GameOptionsAccessor {

    /** {@code allKeys} is {@code final}, so {@link Mutable} is required to swap the array. */
    @Mutable
    @Accessor("allKeys")
    void setAllKeys(KeyBinding[] allKeys);
}
