package me.gabytm.minecraft.arcaneshop.api.shop.price;

import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PriceModifierManager {

    /*@NotNull List<@NotNull ShopPriceModifier> getPlayerModifiers(@NotNull final Player player);

    @NotNull List<@NotNull ShopPriceModifier> getPlayerModifiers(@NotNull final Player player, @NotNull final ShopPriceModifier.Action action);*/

    @NotNull List<@NotNull PriceModifier> getPlayerModifiers(@NotNull final Player player, @NotNull final Shop shop);

}
