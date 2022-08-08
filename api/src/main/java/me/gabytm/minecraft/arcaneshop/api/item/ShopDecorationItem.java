package me.gabytm.minecraft.arcaneshop.api.item;

import org.jetbrains.annotations.NotNull;

public interface ShopDecorationItem {

    @NotNull DisplayItem getDisplayItem();

    int getSlot();

    int getPage();

}
