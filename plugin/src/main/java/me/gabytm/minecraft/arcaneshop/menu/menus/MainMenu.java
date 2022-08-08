package me.gabytm.minecraft.arcaneshop.menu.menus;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MainMenu {

    private final MenuManager menuManager;
    private final ShopManager shopManager;

    public MainMenu(@NotNull final MenuManager menuManager, @NotNull final ShopManager shopManager) {
        this.menuManager = menuManager;
        this.shopManager = shopManager;
    }

    public void open(@NotNull final Player player) {
        final Gui gui = Gui.gui()
                .title(Component.text("ArcaneShop", NamedTextColor.DARK_PURPLE))
                .rows(6)
                .disableAllInteractions()
                .create();

        for (final Map.Entry<String, Shop> entry : shopManager.getShops().entrySet()) {
            final Shop shop = entry.getValue();

            gui.setItem(shop.getMainMenuSlot(), new GuiItem(shop.getMainMenuItem(), event -> {
                if (player.hasPermission("arcaneshop.shop." + entry.getKey())) {
                    player.sendMessage("Opening shop " + entry.getKey());
                    menuManager.openShop(player, shop, 1);
                } else {
                    player.sendMessage("You can not access shop " + entry.getKey());
                    player.closeInventory();
                }
            }));
        }

        gui.open(player);
    }

}
