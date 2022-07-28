package me.gabytm.minecraft.arcaneshop.api.shop;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ShopItem {

    @NotNull ItemStack item();

    int getSlot();

    int getPage();

    double getBuyPrice();

    double getSellPrice();

}
