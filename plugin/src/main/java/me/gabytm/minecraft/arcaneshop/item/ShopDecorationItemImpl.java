package me.gabytm.minecraft.arcaneshop.item;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;

public class ShopDecorationItemImpl implements ShopDecorationItem {

    private final DisplayItem displayItem;
    private final List<@NotNull Integer> slots;
    private final int page;

    public ShopDecorationItemImpl(
            @NotNull final DisplayItem displayItem, final List<@NotNull @Range(from = 0, to = 53) Integer> slots,
            @Range(from = -1, to = Integer.MAX_VALUE) final int page
    ) {
        this.displayItem = displayItem;
        this.slots = slots;
        this.page = page;
    }

    @Override
    public @NotNull DisplayItem getDisplayItem() {
        return displayItem;
    }

    @Override
    public @NotNull List<@NotNull Integer> getSlots() {
        return slots;
    }

    @Override
    public int getPage() {
        return page;
    }

}
