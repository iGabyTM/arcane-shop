package me.gabytm.minecraft.arcaneshop.shop.price;

import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.price.PriceModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PriceModifierImpl implements PriceModifier {

    private final String name;
    private final Action action;
    private final Set<String> shops;
    private final Set<String> items;
    private final double value;

    public PriceModifierImpl(String name, Action action, Set<String> shops, Set<String> items, double value) {
        this.name = name;
        this.action = action;
        this.shops = shops;
        this.items = items;
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public @NotNull Set<@NotNull String> getShops() {
        return shops;
    }

    @Override
    public @NotNull Set<@NotNull String> getItems() {
        return items;
    }

    @Override
    public double getValue() {
        return value;
    }

}
