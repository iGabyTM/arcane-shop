package me.gabytm.minecraft.arcaneshop.api.shop;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ShopManager {

    @Nullable Shop getShop(@NotNull final String name);

    @NotNull Map<String, Shop> getShops();

}
