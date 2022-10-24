package me.gabytm.minecraft.arcaneshop.item.custom.handlers;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemHandler;
import me.gabytm.minecraft.arcaneshop.api.util.collection.Pair;
import me.gabytm.minecraft.arcaneshop.item.custom.CustomItemHandlerImpl;
import me.gabytm.minecraft.arcaneshop.util.Items;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.spongepowered.configurate.ConfigurationNode;

public class HeadDatabaseCustomItemHandler implements CustomItemHandler<HeadDatabaseCustomItemProperties> {

    private final HeadDatabaseAPI api = new HeadDatabaseAPI();

    @Override
    public @NotNull Pair<@NotNull ItemStack, @NotNull HeadDatabaseCustomItemProperties> createItem(@NotNull ItemStack base, @NotNull ConfigurationNode config) {
        final String id = config.node("id").getString();

        if (id == null) {
            return Pair.of(ItemBuilder.skull().build(), new HeadDatabaseCustomItemProperties(""));
        }

        final ItemStack skull = api.getItemHead(id);
        return Pair.of((skull == null) ? ItemBuilder.skull().build() : skull, new HeadDatabaseCustomItemProperties(id));
    }

    @Override
    public void giveItems(@NotNull Player player, @NotNull HeadDatabaseCustomItemProperties properties, int amount) {
        final ItemStack skull = api.getItemHead(properties.getId());

        if (skull == null) {
            Logging.warning("Could not find HDB skull with id " + properties.getId());
            return;
        }

        skull.setAmount(amount);
        player.getInventory().addItem(skull);
    }

    @Override
    public int takeItems(@NotNull Player player, @NotNull HeadDatabaseCustomItemProperties properties, @Range(from = ALL_ITEMS, to = MAX_ITEMS_IN_INVENTORY) int amountToTake) {
        return CustomItemHandlerImpl.takeItems(
                player,
                properties,
                amountToTake,
                item -> Items.isPlayerHead(item) && properties.getId().equals(api.getItemID(item))
        );
    }

    @Override
    public boolean canTakeItems() {
        return true;
    }

}
