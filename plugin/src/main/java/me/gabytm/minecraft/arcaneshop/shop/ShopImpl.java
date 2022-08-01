package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShopImpl implements Shop {

    private final ItemStack displayItem;
    private final Component title;
    private final int slot;
    private final List<@NotNull ShopItem> items;
    private final int pages;
    private final EconomyProvider economyProvider;

    public ShopImpl(
            @NotNull final ItemStack displayItem, @NotNull final Component title,
            final int slot, @NotNull final List<@NotNull ShopItem> items,
            @NotNull final EconomyProvider economyProvider
    ) {
        this.displayItem = displayItem;
        this.title = title;
        this.slot = slot;
        this.items = items;
        this.economyProvider = economyProvider;
        this.pages = (int) items.stream()
                .map(ShopItem::getPage)
                .distinct()
                .count();
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

}
