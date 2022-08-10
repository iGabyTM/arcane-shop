package me.gabytm.minecraft.arcaneshop.menu;

import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
import me.gabytm.minecraft.arcaneshop.menu.menus.amountselector.AmountSelectorMenu;
import me.gabytm.minecraft.arcaneshop.menu.menus.MainMenu;
import me.gabytm.minecraft.arcaneshop.menu.menus.ShopMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuManager {

    private final AmountSelectorMenu amountSelectorMenu;
    private final MainMenu mainMenu;
    private final ShopMenu shopMenu = new ShopMenu(this);

    public MenuManager(@NotNull final ShopManager shopManager, @NotNull final ConfigManager configManager) {
        this.amountSelectorMenu = new AmountSelectorMenu(this, configManager);
        this.mainMenu = new MainMenu(this, shopManager);
    }

    public void openAmountSelectorForBuy(@NotNull final ShopItem item, @NotNull final Player player, @NotNull final Shop shop, final int page) {
        amountSelectorMenu.open(item, player, shop, page, true);
    }

    public void openAmountSelectorForSell(@NotNull final ShopItem item, @NotNull final Player player, @NotNull final Shop shop, final int page) {
        amountSelectorMenu.open(item, player, shop, page, false);
    }

    public void openMain(@NotNull final Player player) {
        mainMenu.open(player);
    }

    public void openShop(@NotNull final Player player, @NotNull final Shop shop, final int page) {
        shopMenu.open(player, shop, page);
    }

}
