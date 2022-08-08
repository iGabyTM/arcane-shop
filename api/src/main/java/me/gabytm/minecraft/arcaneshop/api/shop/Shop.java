package me.gabytm.minecraft.arcaneshop.api.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface Shop {

    @NotNull ItemStack getDisplayItem();

    @NotNull Component getTitle();

    int getSlot();

    @NotNull List<@NotNull ShopItem> getItems();

    int getPages();

    @NotNull List<@NotNull ShopDecorationItem> getDecorations();

    @NotNull EconomyProvider getEconomyProvider();

    @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getShopActions();

}
