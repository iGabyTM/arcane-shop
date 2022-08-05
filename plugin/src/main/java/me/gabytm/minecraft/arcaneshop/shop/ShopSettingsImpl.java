package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopSettings;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ShopSettingsImpl implements ShopSettings {

    private final EconomyProvider economyProvider;
    private final Map<@NotNull ShopAction, @NotNull ClickType> clickActions;

    public ShopSettingsImpl(@NotNull final EconomyProvider economyProvider, @NotNull final Map<@NotNull ShopAction, @NotNull ClickType> clickActions) {
        this.economyProvider = economyProvider;
        this.clickActions = clickActions;
    }

    @Override
    public @NotNull EconomyProvider getEconomyProvider() {
        return economyProvider;
    }

    @Override
    public @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getClickActions() {
        return clickActions;
    }

}
