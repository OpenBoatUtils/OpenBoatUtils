package dev.o7moon.openboatutils.mixin;

import dev.o7moon.openboatutils.OpenBoatUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;setCameraType(Lnet/minecraft/client/CameraType;)V"))
    private void openboatutils$cycleToAllowedCamera(Options options, CameraType proposed) {
        CameraType candidate = proposed;

        // Walk through every camera type at most once, starting at vanilla's proposed one.
        for (int i = 0; i < CameraType.values().length; i++) {
            if (openboatutils$isAllowed(candidate)) {
                options.setCameraType(candidate);
                return;
            }
            candidate = candidate.cycle();
        }
        // No camera type is allowed: leave the current one untouched.
    }

    @Unique
    private static boolean openboatutils$isAllowed(CameraType cameraType) {
        OpenBoatUtils utils = OpenBoatUtils.instance;
        return switch (cameraType) {
            case FIRST_PERSON -> utils.getAllowFirstPerson();
            case THIRD_PERSON_BACK -> utils.getAllowThirdPerson();
            case THIRD_PERSON_FRONT -> utils.getAllowThirdPersonFront();
        };
    }
}