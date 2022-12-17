package me.gabytm.minecraft.arcaneshop.api;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.api.shop.price.PriceModifierManager;
import org.jetbrains.annotations.NotNull;

public class ArcaneShopAPIImpl implements ArcaneShopAPI {

    private final CustomItemManager customItemManager;
    private final EconomyManager economyManager;
    private final ShopManager shopManager;
    private final PriceModifierManager priceModifierManager;

    public ArcaneShopAPIImpl(@NotNull final CustomItemManager customItemManager, @NotNull final EconomyManager economyManager,
                             @NotNull final ShopManager shopManager, @NotNull final PriceModifierManager priceModifierManager) {
        this.customItemManager = customItemManager;
        this.economyManager = economyManager;
        this.shopManager = shopManager;
        this.priceModifierManager = priceModifierManager;
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

    @Override
    public @NotNull PriceModifierManager getPriceModifierManager() {
        return priceModifierManager;
    }

}
