package me.gabytm.minecraft.arcaneshop.item.head.providers;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.gabytm.minecraft.arcaneshop.api.item.head.HeadTextureProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HdbHeadTextureProvider implements HeadTextureProvider {

    private final HeadDatabaseAPI api = new HeadDatabaseAPI();

    @Override
    public @NotNull ItemStack applyTexture(@NotNull String id) {
        final ItemStack skull = api.getItemHead(id);
        return (skull == null) ? ItemBuilder.skull().build() : skull;
    }

}
