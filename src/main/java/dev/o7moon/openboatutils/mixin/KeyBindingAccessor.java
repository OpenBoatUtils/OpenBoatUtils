package dev.o7moon.openboatutils.mixin;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

    /**
     * The {@code translationKey -> KeyBinding} registry. The {@link KeyBinding} constructor
     * adds itself here automatically; we only need it to remove our dynamic binds again.
     */
    @Accessor("KEYS_BY_ID")
    static Map<String, KeyBinding> getKeysById() {
        throw new AssertionError();
    }

    //? >=1.21.7 {
    // 1.21.7+ replaced the string categories (and CATEGORY_ORDER_MAP) with KeyBinding.Category
    // objects that order themselves by registration, so there is nothing to expose here.
    //? } else {
    /**
     * The {@code category -> order} map used by {@link KeyBinding#compareTo}. A category that is
     * missing here causes an NPE while the controls screen sorts its bindings, so our category
     * must be present before any of our binds reach the screen.
     */
    @Accessor("CATEGORY_ORDER_MAP")
    static Map<String, Integer> getCategoryOrderMap() {
        throw new AssertionError();
    }
    //? }
}
