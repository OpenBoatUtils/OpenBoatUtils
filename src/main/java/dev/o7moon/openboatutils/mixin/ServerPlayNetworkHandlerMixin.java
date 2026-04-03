package dev.o7moon.openboatutils.mixin;

//? >= 1.21.6 {
/*import dev.o7moon.openboatutils.OpenBoatUtils;
*///? }
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Inject(method = "isMovementInvalid", at = @At("HEAD"), cancellable = true)
    private static void isMovementInvalid(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);// !!! this disables the movement anti-cheat, but is necessary for stepping to work in singleplayer
    }

    // Force the flag that triggers moved wrongly to false

    //? <1.21.6 {
    @ModifyVariable(method = "onVehicleMove", at = @At("STORE"), ordinal = 2)
    private boolean onVehicleMove_WronglyFlag(boolean original) {
        return false;
    }
    //?}

    //? >=1.21.6 {
    /*@ModifyConstant(method = "onVehicleMove", constant = @Constant(doubleValue = 0.0625))
    private double preventMovedWrongly(double constant) {
        return OpenBoatUtils.instance.getActiveContext() != null ? Double.MAX_VALUE : constant;
    }
    *///?}
}
