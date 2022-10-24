package me.gabytm.minecraft.arcaneshop.item.custom;

import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemHandler;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;
import me.gabytm.minecraft.arcaneshop.item.custom.handlers.HeadDatabaseCustomItemProperties;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Function;

public abstract class CustomItemHandlerImpl<P extends CustomItemProperties> implements CustomItemHandler<P> {

    /**
     * Basic implementation of {@link CustomItemHandler#takeItems(Player, CustomItemProperties, int)} that should fit most cases
     *
     * @param player       player
     * @param properties   properties
     * @param amountToTake amount of items to take
     * @param validator    function to check if the item is valid
     * @return amount of items took
     */
    public static int takeItems(
            @NotNull Player player, @NotNull HeadDatabaseCustomItemProperties properties,
            @Range(from = ALL_ITEMS, to = MAX_ITEMS_IN_INVENTORY) int amountToTake, @NotNull final Function<@NotNull ItemStack, @NotNull Boolean> validator
    ) {
        int itemsTook = 0;

        for (int i = 0; i < 36; i++) {
            final ItemStack item = player.getInventory().getItem(i);

            // Check if the item is null, not a head or its ID (if it has one) is not the one we are looking for
            if (item == null || !validator.apply(item)) {
                continue;
            }

            // If all items should be taken or the item's amount + itemsTook doesn't exceed amountToTake, remove the item
            if ((amountToTake == ALL_ITEMS) || (itemsTook + item.getAmount() <= amountToTake)) {
                itemsTook += item.getAmount();
                player.getInventory().setItem(i, null);
                continue;
            }

            // Take only how many is needed until amountToTake and exit the loop
            final int itemsLeftToTake = amountToTake - itemsTook;
            itemsTook += itemsLeftToTake;
            item.setAmount(item.getAmount() - itemsLeftToTake);
            break;
        }

        return itemsTook;
    }

}
