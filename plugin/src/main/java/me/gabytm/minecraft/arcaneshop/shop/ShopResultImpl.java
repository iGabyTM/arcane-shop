package me.gabytm.minecraft.arcaneshop.shop;

import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopResult;
import org.jetbrains.annotations.NotNull;

public class ShopResultImpl implements ShopResult {

    private final Result result;
    private final ShopAction action;
    private final Shop shop;
    private final ShopItem item;
    private final int amount;
    private final double price;

    public static @NotNull ShopResult buy(@NotNull final Result result, @NotNull final Shop shop, @NotNull final ShopItem item, final int amount, final double price) {
        return new ShopResultImpl(result, ShopAction.BUY, shop, item, amount, price);
    }

    public static @NotNull ShopResult sell(@NotNull final Result result, @NotNull final Shop shop, @NotNull final ShopItem item, final int amount, final double price) {
        return new ShopResultImpl(result, ShopAction.SELL, shop, item, amount, price);
    }

    private ShopResultImpl(@NotNull final Result result, @NotNull final ShopAction action, @NotNull final Shop shop, @NotNull final ShopItem item, final int amount, final double price) {
        this.result = result;
        this.action = action;
        this.shop = shop;
        this.item = item;
        this.amount = amount;
        this.price = price;
    }

    @Override
    public @NotNull Result getResult() {
        return result;
    }

    @Override
    public @NotNull ShopAction getAction() {
        return action;
    }

    @Override
    public @NotNull Shop getShop() {
        return shop;
    }

    @Override
    public @NotNull ShopItem getItem() {
        return item;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public double getPrice() {
        return price;
    }

}
