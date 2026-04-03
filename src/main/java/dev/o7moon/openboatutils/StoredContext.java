package dev.o7moon.openboatutils;

import net.minecraft.util.Identifier;

public class StoredContext extends MutableContext {

    private final Identifier identifier;

    StoredContext(Identifier identifier) {
        this.identifier = identifier;
        this.applyFrom(ISettingContext.VANILLA);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public void switchTo() {}
}
