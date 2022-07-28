package me.gabytm.minecraft.arcaneshop.api;

import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import org.jetbrains.annotations.NotNull;

public class ArcaneShopAPIImpl implements ArcaneShopAPI {

    private final ShopManager shopManager;

    public ArcaneShopAPIImpl(@NotNull final ShopManager shopManager) {
        this.shopManager = shopManager;
    }

    @Override
    public @NotNull ShopManager getShopManager() {
        return shopManager;
    }

}
