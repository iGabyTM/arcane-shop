package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ShopImpl implements Shop {

    private final ItemStack displayItem;
    private final Component title;
    private final int slot;
    private final List<@NotNull ShopItem> items;
    private final int pages;
    private final EconomyProvider economyProvider;
    private final Map<@NotNull ShopAction, @NotNull ClickType> clickActions;

    public ShopImpl(
            @NotNull final ItemStack displayItem, @NotNull final Component title,
            final int slot, @NotNull final List<@NotNull ShopItem> items,
            @NotNull final EconomyProvider economyProvider, @NotNull final Map<@NotNull ShopAction, @NotNull ClickType> clickActions
    ) {
        this.displayItem = displayItem;
        this.title = title;
        this.slot = slot;
        this.items = items;
        this.pages = (int) items.stream().map(ShopItem::getPage).distinct().count();
        this.economyProvider = economyProvider;
        this.clickActions = clickActions;
    }

    @Override
    public @NotNull ItemStack getDisplayItem() {
        return displayItem;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public int getSlot() {
        return slot;
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
    public @NotNull EconomyProvider getEconomyProvider() {
        return economyProvider;
    }

    @Override
    public @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getClickActions() {
        return clickActions;
    }

}
