package me.gabytm.minecraft.arcaneshop.menu;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MainMenu {

    public final ShopManager shopManager;

    public MainMenu(ShopManager shopManager) {
        this.shopManager = shopManager;
    }

    public void open(@NotNull final Player player) {
        final Gui gui = Gui.gui()
                .title(Component.text("ArcaneShop"))
                .rows(6)
                .disableAllInteractions()
                .create();

        for (final Map.Entry<String, Shop> entry : shopManager.getShops().entrySet()) {
            final Shop shop = entry.getValue();

            gui.setItem(shop.getSlot(), new GuiItem(shop.getDisplayItem(), event -> {
                if (player.hasPermission("arcaneshop.shop." + entry.getKey())) {
                    player.sendMessage("Opening shop " + entry.getKey());
                    new ShopMenu().open(player, shop, 1);
                } else {
                    player.sendMessage("You can not access shop " + entry.getKey());
                    player.closeInventory();
                }
            }));
        }

        gui.open(player);
    }

}
