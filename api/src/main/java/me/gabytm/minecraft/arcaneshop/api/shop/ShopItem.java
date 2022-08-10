package me.gabytm.minecraft.arcaneshop.api.shop;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ShopItem {

    @NotNull DisplayItem displayItem();

    @NotNull ItemStack itemStack();

    int getSlot();

    int getPage();

    double getBuyPrice();

    double getSellPrice();

    boolean acceptOnlyExactItems();

}
