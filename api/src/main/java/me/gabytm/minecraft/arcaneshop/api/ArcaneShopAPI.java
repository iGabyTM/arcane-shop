package me.gabytm.minecraft.arcaneshop.api;

import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import org.jetbrains.annotations.NotNull;

public interface ArcaneShopAPI {

    @NotNull ShopManager getShopManager();

}
