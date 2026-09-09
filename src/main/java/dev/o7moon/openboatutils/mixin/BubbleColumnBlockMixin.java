package dev.o7moon.openboatutils.mixin;

import net.minecraft.block.BlockState;import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.state.property.Property;import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BubbleColumnBlock.class)
public class BubbleColumnBlockMixin {
    //? >= 1.21.3 {
    @ModifyVariable(method = "onEntityCollision", at = @At("STORE"))
    public boolean hookConstantSet(boolean world) {
        return false;
    }

    @Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;"))
    private Comparable<Boolean> abc(BlockState instance, Property<Boolean> property) {
        return false;
    }
    //? }
}
