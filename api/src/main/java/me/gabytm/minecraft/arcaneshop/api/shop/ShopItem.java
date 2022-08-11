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

    boolean acceptOnlyExactItems();

}
