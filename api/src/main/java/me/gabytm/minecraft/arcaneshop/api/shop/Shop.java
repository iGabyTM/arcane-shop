package me.gabytm.minecraft.arcaneshop.api.shop;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Shop {

    @NotNull ItemStack getDisplayItem();

    @NotNull Component getTitle();

    int getSlot();

    @NotNull List<@NotNull ShopItem> getItems();

    int getPages();

    @NotNull ShopSettings settings();

}
