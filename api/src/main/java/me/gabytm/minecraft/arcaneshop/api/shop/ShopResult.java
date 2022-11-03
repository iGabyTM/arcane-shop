package me.gabytm.minecraft.arcaneshop.api.shop;

import org.jetbrains.annotations.NotNull;

public interface ShopResult {

    @NotNull Result getResult();

    /**
     * Can be either {@link ShopAction#BUY} or {@link ShopAction#SELL}
     */
    @NotNull ShopAction getAction();

    @NotNull Shop getShop();

    @NotNull ShopItem getItem();

    int getAmount();

    double getPrice();

    default boolean success() {
        return getResult().success();
    }

    enum Result {

        /**
         * Results for both actions, sell and buy
         */
        SUCCESS(true),
        SOLD_PARTIALLY(true),
        ITEM_IS_NULL(false),

        /**
         * Results for 'buy'
         */
        NOT_ENOUGH_MONEY(false),
        COULD_NOT_SUBTRACT_MONEY(false),

        /**
         * Results for 'sell'
         */
        ITEM_IS_COMMAND(false),
        NO_ITEMS_IN_INVENTORY(false);

        private final boolean success;

        Result(final boolean success) {
            this.success = success;
        }

        public boolean success() {
            return success;
        }

    }

}
