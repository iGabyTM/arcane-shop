package me.gabytm.minecraft.arcaneshop.menu.menus.amountselector;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import org.jetbrains.annotations.NotNull;

public class AmountSelectorButton {

    private final DisplayItem displayItem;
    private final AmountAction action;
    private final int value;
    private final int slot;

    public AmountSelectorButton(
            @NotNull final DisplayItem displayItem, @NotNull final AmountAction action,
            final int value, final int slot
    ) {
        this.displayItem = displayItem;
        this.action = action;
        this.value = value;
        this.slot = slot;
    }

    public DisplayItem getDisplayItem() {
        return displayItem;
    }

    public AmountAction getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }

    public int getSlot() {
        return slot;
    }

}
