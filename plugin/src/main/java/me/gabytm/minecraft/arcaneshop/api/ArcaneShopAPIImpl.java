package me.gabytm.minecraft.arcaneshop.api;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import org.jetbrains.annotations.NotNull;

public class ArcaneShopAPIImpl implements ArcaneShopAPI {

    private final CustomItemManager customItemManager;
    private final EconomyManager economyManager;
    private final ShopManager shopManager;

    public ArcaneShopAPIImpl(CustomItemManager customItemManager, @NotNull final EconomyManager economyManager,
                             @NotNull final ShopManager shopManager
    ) {
        this.customItemManager = customItemManager;
        this.economyManager = economyManager;
        this.shopManager = shopManager;
    }

    @Override
    public @NotNull CustomItemManager getCustomItemManager() {
        return customItemManager;
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
