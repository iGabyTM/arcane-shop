package me.gabytm.minecraft.arcaneshop.api.shop.price;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface PriceModifier {

    @NotNull String getName();

    @NotNull Action getAction();

    @NotNull Set<@NotNull String> getShops();

    @NotNull Set<@NotNull String> getItems();

    double getValue();

    enum Action {

        BUY, SELL, BOTH

    }

}
