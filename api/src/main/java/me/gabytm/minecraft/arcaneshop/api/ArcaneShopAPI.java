package me.gabytm.minecraft.arcaneshop.api;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import org.jetbrains.annotations.NotNull;

public interface ArcaneShopAPI {

    @NotNull CustomItemManager getCustomItemManager();

    @NotNull EconomyManager getEconomyManager();

    @NotNull ShopManager getShopManager();

}
