package me.gabytm.minecraft.arcaneshop.api.item;

import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;
import me.gabytm.minecraft.arcaneshop.api.util.adventure.WrappedComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DisplayItem {

    /**
     * Returns the {@link ItemStack} wrapped by this object
     *
     * @return {@link ItemStack}
     */
    @NotNull ItemStack getItemStack();

    /**
     * Returns the name read from config
     *
     * @return name read from config
     * @see WrappedComponent#raw()
     */
    @NotNull String name();

    /**
     * Returns the lore read from config
     *
     * @return lore read from config
     * @see WrappedComponent#raw()
     */
    @NotNull List<@NotNull String> lore();

    /**
     * Whether the item is custom
     *
     * @return whether the item is custom
     */
    boolean isCustom();

    /**
     * Returns the custom type of the item, must be accessed only if {@link #isCustom()} is true
     *
     * @return the custom type of the item
     */
    @NotNull String getCustomItemHandlerName();

    /**
     * Returns the custom properties of the item,  must be accessed only if {@link #isCustom()} is true
     *
     * @return the custom properties of the item
     */
    @NotNull CustomItemProperties getCustomItemProperties();

}
