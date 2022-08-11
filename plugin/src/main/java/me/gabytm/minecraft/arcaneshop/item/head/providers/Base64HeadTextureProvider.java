package me.gabytm.minecraft.arcaneshop.item.head.providers;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.gabytm.minecraft.arcaneshop.api.item.head.HeadTextureProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Base64HeadTextureProvider implements HeadTextureProvider {

    @Override
    public @NotNull ItemStack applyTexture(@NotNull String texture) {
        return ItemBuilder.skull().texture(texture).build();
    }

}
