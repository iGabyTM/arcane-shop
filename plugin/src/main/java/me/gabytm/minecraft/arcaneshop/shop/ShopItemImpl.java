package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ShopItemImpl implements ShopItem {

    private final String id;
    private final DisplayItem displayItem;
    private final DisplayItem item;
    private final List<@NotNull String> commands;
    private final boolean executeCommandsOnceForAllItems;
    private final int amount;
    private final int slot;
    private final int page;
    private final double buyPrice;
    private final double sellPrice;

    private final boolean acceptOnlyExactItems;

    public ShopItemImpl(
            @NotNull final String id, @NotNull final DisplayItem displayItem, @Nullable final DisplayItem item,
            @NotNull final List<@NotNull String> commands, final boolean executeCommandsOnceForAllItems,
            final int amount, final int slot, final int page,
            final double buyPrice, final double sellPrice, final boolean acceptOnlyExactItems
    ) {
        this.id = id;
        this.displayItem = displayItem;
        this.item = item;
        this.commands = commands;
        this.executeCommandsOnceForAllItems = executeCommandsOnceForAllItems;
        this.amount = amount;
        this.slot = slot;
        this.page = page;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.acceptOnlyExactItems = acceptOnlyExactItems;
    }

    public ShopItemImpl(@NotNull final String id, @NotNull final DisplayItem displayItem) {
        this(id, displayItem, displayItem, Collections.emptyList(), false, 1, 1, 1, 0.0d, 0.0d, true);
    }

    @Override
    public @NotNull String getId() {
        return id;
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
    public @NotNull List<@NotNull String> getCommands() {
        return commands;
    }

    @Override
    public boolean executeCommandsOnceForAllItems() {
        return executeCommandsOnceForAllItems;
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
