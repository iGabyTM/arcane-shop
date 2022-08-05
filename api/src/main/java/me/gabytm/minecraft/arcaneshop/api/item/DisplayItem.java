package me.gabytm.minecraft.arcaneshop.api.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DisplayItem {

    @NotNull ItemStack item();

    @NotNull String name();

    @NotNull List<@NotNull String> lore();

}
