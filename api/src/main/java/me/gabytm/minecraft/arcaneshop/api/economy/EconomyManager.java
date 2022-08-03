package me.gabytm.minecraft.arcaneshop.api.economy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EconomyManager {

    void register(@NotNull final String name, @NotNull final EconomyProvider provider);

    @Nullable EconomyProvider getProvider(@NotNull final String name);

}
