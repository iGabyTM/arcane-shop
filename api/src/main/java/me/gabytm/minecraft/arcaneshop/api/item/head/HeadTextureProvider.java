package me.gabytm.minecraft.arcaneshop.api.item.head;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface HeadTextureProvider {

    @NotNull ItemStack applyTexture(@NotNull final String texture);

}
