package me.gabytm.minecraft.arcaneshop.api.shop;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ShopManager {

    @Nullable Shop getShop(@NotNull final String name);

    @NotNull Map<String, Shop> getShops();

    @NotNull ShopResult buyItem(@NotNull final Shop shop, @NotNull final ShopItem item, final int amount, @NotNull final Player player);

    @NotNull ShopResult sellItem(@NotNull final Shop shop, @NotNull final ShopItem item, final int amount, @NotNull final Player player);

}
