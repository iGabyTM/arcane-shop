package me.gabytm.minecraft.arcaneshop.item.custom.handlers;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.gabytm.minecraft.arcaneshop.api.util.collection.Pair;
import me.gabytm.minecraft.arcaneshop.item.custom.CustomItemHandlerImpl;
import me.gabytm.minecraft.arcaneshop.util.Items;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.spongepowered.configurate.ConfigurationNode;

public class TestCustomItemHandler extends CustomItemHandlerImpl<TestCustomItemProperties> {

    private final HeadDatabaseAPI api = new HeadDatabaseAPI();

    @Override
    public @NotNull Pair<@NotNull ItemStack, @NotNull TestCustomItemProperties> createItem(@NotNull ItemStack base, @NotNull ConfigurationNode config) {
        final String id = config.node("id").getString();

        if (id == null) {
            return Pair.of(ItemBuilder.skull().build(), new TestCustomItemProperties(""));
        }

        final ItemStack skull = api.getItemHead(id);
        return Pair.of((skull == null) ? ItemBuilder.skull().build() : skull, new TestCustomItemProperties(id));
    }

    @Override
    public void giveItems(@NotNull Player player, @NotNull TestCustomItemProperties properties, int amount) {
        final ItemStack skull = api.getItemHead(properties.getId());

        if (skull == null) {
            Logging.warning("Could not find HDB skull with id " + properties.getId());
            return;
        }

        skull.setAmount(amount);
        player.getInventory().addItem(skull);
    }

    @Override
    public int takeItems(@NotNull Player player, @NotNull TestCustomItemProperties properties, @Range(from = ALL_ITEMS, to = MAX_ITEMS_IN_INVENTORY) int amountToTake) {
        int itemsTook = 0;

        for (int i = 0; i < 36; i++) {
            final ItemStack item = player.getInventory().getItem(i);

            // Check if the item is null, not a head or its ID (if it has one) is not the one we are looking for
            if (item == null || !Items.isPlayerHead(item) || !properties.getId().equals(api.getItemID(item))) {
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

    @Override
    public boolean canTakeItems() {
        return true;
    }

}
