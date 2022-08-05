package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShopItemImpl implements ShopItem {

    private final DisplayItem displayItem;
    private final ItemStack item;
    private final int slot;
    private final int page;
    private final double buyPrice;
    private final double sellPrice;

    public ShopItemImpl(
            @NotNull final DisplayItem displayItem, @NotNull final ItemStack item, final int slot,
            final int page, final double buyPrice, final double sellPrice
    ) {
        this.displayItem = displayItem;
        this.item = item;
        this.slot = slot;
        this.page = page;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    @Override
    public @NotNull DisplayItem displayItem() {
        return displayItem;
    }

    @Override
    public @NotNull ItemStack itemStack() {
        return item;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public double getBuyPrice() {
        return buyPrice;
    }

    @Override
    public double getSellPrice() {
        return sellPrice;
    }

}
