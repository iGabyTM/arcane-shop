package me.gabytm.minecraft.arcaneshop.item;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;
import me.gabytm.minecraft.arcaneshop.item.custom.CustomItemPropertiesImpl;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DisplayItemImpl implements DisplayItem {

    private final ItemStack item;
    private final String name;
    private final List<@NotNull String> lore;

    private final boolean isCustom;
    private final String customItemHandlerName;
    private final CustomItemProperties customItemProperties;

    public DisplayItemImpl(
            @NotNull final ItemStack item, @NotNull final String name, @NotNull final List<@NotNull String> lore,
            final boolean isCustom, @NotNull final String customItemHandlerName, @NotNull final CustomItemProperties customItemProperties
    ) {
        this.item = item;
        this.name = name;
        this.lore = lore;
        this.isCustom = isCustom;
        this.customItemHandlerName = customItemHandlerName;
        this.customItemProperties = customItemProperties;
    }

    public DisplayItemImpl(@NotNull final ItemStack item, @NotNull final String name, @NotNull final List<@NotNull String> lore) {
        this(item, name, lore, false, "", CustomItemPropertiesImpl.EMPTY);
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
    public boolean isCustom() {
        return isCustom;
    }

    @Override
    public @NotNull String getCustomItemHandlerName() {
        return customItemHandlerName;
    }

    @Override
    public @NotNull CustomItemProperties getCustomItemProperties() {
        return customItemProperties;
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
