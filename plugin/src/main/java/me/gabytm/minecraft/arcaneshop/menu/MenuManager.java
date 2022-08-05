package me.gabytm.minecraft.arcaneshop.menu;

import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.menu.menus.AmountSelectorMenu;
import me.gabytm.minecraft.arcaneshop.menu.menus.MainMenu;
import me.gabytm.minecraft.arcaneshop.menu.menus.ShopMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuManager {

    private final AmountSelectorMenu amountSelectorMenu = new AmountSelectorMenu(this);
    private final MainMenu mainMenu;
    private final ShopMenu shopMenu = new ShopMenu(this);

    public MenuManager(@NotNull final ShopManager shopManager) {
        this.mainMenu = new MainMenu(this, shopManager);
    }

    public void openAmountSelector(@NotNull final ShopItem item, @NotNull final Player player, @NotNull final Shop shop, final int page) {
        amountSelectorMenu.open(item, player, shop, page);
    }

    public void openMain(@NotNull final Player player) {
        mainMenu.open(player);
    }

    public void openShop(@NotNull final Player player, @NotNull final Shop shop, final int page) {
        shopMenu.open(player, shop, page);
    }

}
