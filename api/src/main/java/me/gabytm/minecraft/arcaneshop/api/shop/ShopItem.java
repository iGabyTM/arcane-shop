package me.gabytm.minecraft.arcaneshop.api.shop;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShopItem {

    /**
     * The item that is displayed in the GUI
     */
    @NotNull DisplayItem getDisplayItem();

    /**
     * The item that is sold/bought
     */
    @Nullable DisplayItem getItem();

    /**
     * The starting amount of items
     */
    int getAmount();

    /**
     * The position of the item
     */
    int getSlot();

    /**
     * The page where the item is located
     *
     * @return the page number
     */
    int getPage();

    /**
     * The buy price for one item
     */
    double getBuyPrice();

    /**
     * The sell price for one item
     */
    double getSellPrice();

    /**
     * Whether the item can be bought
     *
     * @return {@link #getBuyPrice()} > 0.0
     */
    default boolean canBeBought() {
        return getBuyPrice() > 0.0d;
    }

    /**
     * Whether the item can be sold
     *
     * @return {@link #getBuyPrice()} > 0.0
     */
    default boolean canBeSold() {
        return getSellPrice() > 0.0d;
    }

    boolean acceptOnlyExactItems();

}
