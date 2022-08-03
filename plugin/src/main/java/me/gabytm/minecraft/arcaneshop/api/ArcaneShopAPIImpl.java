package me.gabytm.minecraft.arcaneshop.api;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import org.jetbrains.annotations.NotNull;

public class ArcaneShopAPIImpl implements ArcaneShopAPI {

    private final EconomyManager economyManager;
    private final ShopManager shopManager;

    public ArcaneShopAPIImpl(@NotNull final EconomyManager economyManager, @NotNull final ShopManager shopManager) {
        this.economyManager = economyManager;
        this.shopManager = shopManager;
    }

    @Override
    public @NotNull EconomyManager getEconomyManager() {
        return economyManager;
    }

    @Override
    public @NotNull ShopManager getShopManager() {
        return shopManager;
    }

}
