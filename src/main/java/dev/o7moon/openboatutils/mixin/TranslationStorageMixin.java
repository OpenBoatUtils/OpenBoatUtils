package dev.o7moon.openboatutils.mixin;

import dev.o7moon.openboatutils.client.Keybinds;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Resolves the translation keys of our dynamically registered keybinds to the labels the server
 * provided, so the vanilla controls menu shows the server's names without us shipping (or being
 * able to ship) translations for arbitrary server defined strings.
 */
@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {

    @Inject(method = "get(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void openboatutils$keybindLabels(String key, String fallback, CallbackInfoReturnable<String> cir) {
        if (key != null && key.startsWith(Keybinds.TRANSLATION_PREFIX)) {
            String label = Keybinds.LABELS.get(key);

            if (label != null) {
                cir.setReturnValue(label);
            }
        }
    }
}
