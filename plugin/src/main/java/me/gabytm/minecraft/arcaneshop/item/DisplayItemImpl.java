package me.gabytm.minecraft.arcaneshop.item;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DisplayItemImpl implements DisplayItem {

    private final ItemStack item;
    private final String name;
    private final List<@NotNull String> lore;

    public DisplayItemImpl(@NotNull final ItemStack item, @NotNull final String name, @NotNull final List<@NotNull String> lore) {
        this.item = item;
        this.name = name;
        this.lore = lore;
    }

    @Override
    public @NotNull ItemStack item() {
        return item;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull List<@NotNull String> lore() {
        return lore;
    }

    @Override
    public String toString() {
        return "DisplayItemImpl{" +
                "item=" + item +
                ", name='" + name + '\'' +
                ", lore=" + lore +
                '}';
    }

}
