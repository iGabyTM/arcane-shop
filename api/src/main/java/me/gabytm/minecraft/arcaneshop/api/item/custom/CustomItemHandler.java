package me.gabytm.minecraft.arcaneshop.api.item.custom;

import me.gabytm.minecraft.arcaneshop.api.util.collection.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

public interface CustomItemHandler<P extends CustomItemProperties> {

    @NotNull Pair<@NotNull ItemStack, @NotNull P> getItem(@NotNull final ItemStack base, @NotNull final ConfigurationNode config);

    void giveItems(@NotNull final Player player, @NotNull final P properties, final int amount);

}
