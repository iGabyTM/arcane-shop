package me.gabytm.minecraft.arcaneshop.api.shop;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopItem {

    @NotNull DisplayItem displayItem();

    @Nullable DisplayItem getItem();

    int getAmount();

    int getSlot();

    int getPage();

    double getBuyPrice();

    double getSellPrice();

    default boolean canBeBought() {
        return getBuyPrice() > 0.0d;
    }

    default boolean canBeSold() {
        return getSellPrice() > 0.0d;
    }

    boolean acceptOnlyExactItems();

}
