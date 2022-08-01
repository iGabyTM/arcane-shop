package me.gabytm.minecraft.arcaneshop.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ShopMenu {

    public void open(@NotNull final Player player, @NotNull final Shop shop, final int page) {
        final List<ShopItem> items = shop.getItems().stream()
                .filter(it -> it.getPage() == page)
                .collect(Collectors.toList());

        if (items.isEmpty()) {
            return;
        }

        final Gui gui = Gui.gui()
                .title(shop.getTitle())
                .rows(6)
                .disableAllInteractions()
                .create();

        for (final ShopItem item : items) {
            final GuiItem guiItem = ItemBuilder.from(item.item().clone())
                    .lore(lore -> {
                        if (item.getSellPrice() != 0.0d) {
                            lore.add(Component.text("Sell for: $" + item.getSellPrice(), NamedTextColor.GREEN));
                        }

                        if (item.getBuyPrice() != 0.0d) {
                            lore.add(Component.text("Buy for: $" + item.getBuyPrice(), NamedTextColor.RED));
                        }
                    })
                    .asGuiItem(event -> {
                        if (event.getClick() == ClickType.LEFT && item.getSellPrice() != 0.0d) {
                            player.sendMessage("Selling " + item.item().getType() + " for " + item.getSellPrice());
                        } else if (event.getClick() == ClickType.RIGHT && item.getBuyPrice() != 0.0d) {
                            player.sendMessage("Buying " + item.item().getType() + " for " + item.getSellPrice());
                        }
                    });

            gui.setItem(item.getSlot(), guiItem);
        }

        if (page - 1 >= 1) {
            final GuiItem arrow = ItemBuilder.from(Material.ARROW)
                    .name(Component.text("Previous page", NamedTextColor.RED))
                    .amount(page - 1)
                    .asGuiItem(event -> open(player, shop, page - 1));
            gui.setItem(6, 4, arrow);
        }

        if (page + 1 <= shop.getPages()) {
            final GuiItem arrow = ItemBuilder.from(Material.ARROW)
                    .name(Component.text("Next page", NamedTextColor.GREEN))
                    .amount(page + 1)
                    .asGuiItem(event -> open(player, shop, page + 1));
            gui.setItem(6, 6, arrow);
        }

        gui.open(player);
    }

}
