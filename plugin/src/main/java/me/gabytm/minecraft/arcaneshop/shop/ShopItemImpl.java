package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShopItemImpl implements ShopItem {

    private final DisplayItem displayItem;
    private final DisplayItem item;
    private final int amount;
    private final int slot;
    private final int page;
    private final double buyPrice;
    private final double sellPrice;

    private final boolean acceptOnlyExactItems;

    public ShopItemImpl(
            @NotNull final DisplayItem displayItem, @Nullable final DisplayItem item, final int amount,
            final int slot, final int page, final double buyPrice,
            final double sellPrice, final boolean acceptOnlyExactItems
    ) {
        this.displayItem = displayItem;
        this.item = item;
        this.amount = amount;
        this.slot = slot;
        this.page = page;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.acceptOnlyExactItems = acceptOnlyExactItems;
    }

    @Override
    public @NotNull DisplayItem getDisplayItem() {
        return displayItem;
    }

    @Override
    public @Nullable DisplayItem getItem() {
        return item;
    }

    @Override
    public int getAmount() {
        return amount;
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

    @Override
    public boolean acceptOnlyExactItems() {
        return acceptOnlyExactItems;
    }

}
