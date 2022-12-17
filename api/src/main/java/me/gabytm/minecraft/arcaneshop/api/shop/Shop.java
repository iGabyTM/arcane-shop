package me.gabytm.minecraft.arcaneshop.api.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import me.gabytm.minecraft.arcaneshop.api.util.adventure.WrappedComponent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface Shop {

    @NotNull String getName();

    @ApiStatus.Internal
    void setName(@NotNull final String name);

    @NotNull ItemStack getMainMenuItem();

    int getMainMenuSlot();


    @NotNull WrappedComponent getMenuTitle();

    int getMenuRows();


    @NotNull List<@NotNull ShopItem> getItems();

    /**
     * Get an item by its {@link ShopItem#getId() id}
     *
     * @param id the id of the item
     * @return item or null
     */
    default @Nullable ShopItem getItem(@NotNull final String id) {
        return getItems()
                .stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    int getPages();

    @NotNull List<@NotNull ShopDecorationItem> getDecorations();

    @NotNull EconomyProvider getEconomyProvider();

    @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getShopActions();

}
