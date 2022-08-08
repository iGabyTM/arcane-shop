package me.gabytm.minecraft.arcaneshop.item;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class ShopDecorationItemImpl implements ShopDecorationItem {

    private final DisplayItem displayItem;
    private final int slot;
    private final int page;

    public ShopDecorationItemImpl(
            @NotNull final DisplayItem displayItem, @Range(from = 0, to = 53) final int slot,
            @Range(from = -1, to = Integer.MAX_VALUE) final int page
    ) {
        this.displayItem = displayItem;
        this.slot = slot;
        this.page = page;
    }

    @Override
    public @NotNull DisplayItem getDisplayItem() {
        return displayItem;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public int getPage() {
        return page;
    }

}
