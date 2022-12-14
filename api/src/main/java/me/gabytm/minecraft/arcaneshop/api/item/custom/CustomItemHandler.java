package me.gabytm.minecraft.arcaneshop.api.item.custom;

import me.gabytm.minecraft.arcaneshop.api.util.collection.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.spongepowered.configurate.ConfigurationNode;

public interface CustomItemHandler<P extends CustomItemProperties> {

    int ALL_ITEMS = -1;
    int MAX_ITEMS_IN_INVENTORY = 64 * 36; // items * slots

    /**
     * Create a custom item
     *
     * @param base   item
     * @param config config
     * @return pair of {@link ItemStack} representing the custom item and {@link P} representing its properties
     */
    @NotNull Pair<@NotNull ItemStack, @NotNull P> createItem(@NotNull final ItemStack base, @NotNull final ConfigurationNode config);

    /**
     * Give {@code amount}x custom items to a {@link Player}
     *
     * @param player     player
     * @param properties properties
     * @param amount     amount
     */
    void giveItems(@NotNull final Player player, @NotNull final P properties, final int amount);

    /**
     * Take {@code amountToTake}x custom items from a {@link Player} inventory
     *
     * @param player       inventory
     * @param properties   properties
     * @param amountToTake amount to take, use {@link #ALL_ITEMS} to take all items
     * @return how many items were took
     */
    int takeItems(@NotNull final Player player, @NotNull final P properties, @Range(from = ALL_ITEMS, to = MAX_ITEMS_IN_INVENTORY) final int amountToTake);

    /**
     * For some plugins might not be possible / worth to have support for selling items, so this boolean should be
     * checked before using {@link #takeItems(Player, CustomItemProperties, int)}
     *
     * @return whether the implementation can {@link #takeItems(Player, CustomItemProperties, int) take items}
     */
    default boolean canTakeItems() {
        return true;
    }

}
