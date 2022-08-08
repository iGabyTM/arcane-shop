package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Map;

public class ShopImpl implements Shop {

    private final ItemStack mainMenuItem;
    private final int mainMenuSlot;

    private final Component menuTitle;
    private final int menuRows;

    private final List<@NotNull ShopItem> items;
    private final int pages;
    private final List<@NotNull ShopDecorationItem> displayItems;
    private final EconomyProvider economyProvider;
    private final Map<@NotNull ShopAction, @NotNull ClickType> shopActions;

    public ShopImpl(
            @NotNull final ItemStack mainMenuItem, @Range(from = 0, to = 54) final int mainMenuSlot,
            @NotNull final Component menuTitle, @Range(from = 1, to = 6) final int menuRows,
            @NotNull final List<@NotNull ShopItem> items, @NotNull final List<@NotNull ShopDecorationItem> displayItems,
            @NotNull final EconomyProvider economyProvider, @NotNull final Map<@NotNull ShopAction, @NotNull ClickType> shopActions
    ) {
        this.mainMenuItem = mainMenuItem;
        this.mainMenuSlot = mainMenuSlot;
        this.menuTitle = menuTitle;
        this.menuRows = menuRows;
        this.items = items;
        this.pages = (int) items.stream().map(ShopItem::getPage).distinct().count();
        this.displayItems = displayItems;
        this.economyProvider = economyProvider;
        this.shopActions = shopActions;
    }

    @Override
    public @NotNull ItemStack getMainMenuItem() {
        return mainMenuItem;
    }

    @Override
    public int getMainMenuSlot() {
        return mainMenuSlot;
    }


    @Override
    public @NotNull Component getMenuTitle() {
        return menuTitle;
    }

    @Override
    public int getMenuRows() {
        return menuRows;
    }


    @Override
    public @NotNull List<@NotNull ShopItem> getItems() {
        return items;
    }

    @Override
    public int getPages() {
        return pages;
    }

    @Override
    public @NotNull List<ShopDecorationItem> getDecorations() {
        return displayItems;
    }

    @Override
    public @NotNull EconomyProvider getEconomyProvider() {
        return economyProvider;
    }

    @Override
    public @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getShopActions() {
        return shopActions;
    }

}
