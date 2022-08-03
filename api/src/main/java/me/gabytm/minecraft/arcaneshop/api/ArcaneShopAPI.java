package me.gabytm.minecraft.arcaneshop.api;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import org.jetbrains.annotations.NotNull;

public interface ArcaneShopAPI {

    @NotNull EconomyManager getEconomyManager();

    @NotNull ShopManager getShopManager();

}
