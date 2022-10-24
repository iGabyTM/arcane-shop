package me.gabytm.minecraft.arcaneshop.menu.menus.amountselector;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AmountSelectorButton {

    /**
     * @see ItemStack#getMaxStackSize()
     */
    public static final int MAX = -1;

    private final DisplayItem displayItem;
    private final AmountSelectionAction action;
    private final int value;
    private final int slot;

    public AmountSelectorButton(
            @NotNull final DisplayItem displayItem, @NotNull final AmountSelectionAction action,
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

    public AmountSelectionAction getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }

    public int getSlot() {
        return slot;
    }

    public boolean valueIsMax() {
        return getValue() == MAX;
    }

}
