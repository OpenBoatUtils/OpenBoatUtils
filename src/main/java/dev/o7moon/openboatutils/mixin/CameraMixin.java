package dev.o7moon.openboatutils.mixin;

import dev.o7moon.openboatutils.OpenBoatUtils;
import io.github.openboatutils.protocol.channels.OBUSettingsPacket;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "calculateFov", at = @At("RETURN"), cancellable = true)
    private void hookFov(float partialTicks, CallbackInfoReturnable<Float> cir) {
        OBUSettingsPacket.FovMode mode = OpenBoatUtils.instance.getFovMode();
        float modifier = OpenBoatUtils.instance.getFovModifier();

        if (mode == OBUSettingsPacket.FovMode.ADDITIVE && modifier == 0) {
            return;
        }

        float normalFov = cir.getReturnValue();

        float fov = switch (mode) {
            case ADDITIVE -> normalFov + modifier;
            case MULTIPLICATIVE -> normalFov * modifier;
            case SET -> modifier;
        };

        if (fov == 0) return;

        cir.setReturnValue(fov);
    }
}
