package me.gabytm.minecraft.arcaneshop.api.item;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ShopDecorationItem {

    int ALL_PAGES = -1;

    @NotNull DisplayItem getDisplayItem();

    @NotNull List<@NotNull Integer> getSlots();

    int getPage();

}
