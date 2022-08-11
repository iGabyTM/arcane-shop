package me.gabytm.minecraft.arcaneshop.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class Items {

    private static final Material PLAYER_HEAD = ServerVersion.IS_LEGACY ? Material.valueOf("SKULL_ITEM") : Material.PLAYER_HEAD;

    private Items() {
        throw new AssertionError("This class can not be instantiated");
    }

    public static boolean isPlayerHead(@NotNull final Material material, final short damage) {
        return ServerVersion.IS_LEGACY ? (material == PLAYER_HEAD && damage == 3) : (material == PLAYER_HEAD);
    }

    @SuppressWarnings("deprecation")
    public static boolean isPlayerHead(@NotNull final ItemStack item) {
        return isPlayerHead(item.getType(), item.getDurability());
    }

}
