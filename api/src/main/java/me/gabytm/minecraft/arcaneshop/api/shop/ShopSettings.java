package me.gabytm.minecraft.arcaneshop.api.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ShopSettings {

    @NotNull EconomyProvider getEconomyProvider();

    @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getClickActions();

}
