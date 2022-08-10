package me.gabytm.minecraft.arcaneshop.menu.menus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShopMenu {

    private final MenuManager menuManager;

    public ShopMenu(@NotNull final MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void open(@NotNull final Player player, @NotNull final Shop shop, final int page) {
        final List<ShopItem> items = shop.getItems().stream()
                .filter(it -> it.getPage() == page)
                .collect(Collectors.toList());

        if (page != 1 && items.isEmpty()) {
            return;
        }

        final Gui gui = Gui.gui()
                .title(shop.getMenuTitle())
                .rows(shop.getMenuRows())
                .disableAllInteractions()
                .create();

        shop.getDecorations().stream()
                .filter(it -> it.getPage() == -1 || it.getPage() == page)
                .forEach(item -> gui.setItem(item.getSlots(), new GuiItem(item.getDisplayItem().item())));

        for (final ShopItem item : items) {
            final GuiItem guiItem = ItemBuilder.from(item.displayItem().item().clone())
                    .lore(lore -> {
                        if (item.getSellPrice() != 0.0d) {
                            lore.add(Component.text("Sell for: $" + item.getSellPrice(), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                        }

                        if (item.getBuyPrice() != 0.0d) {
                            lore.add(Component.text("Buy for: $" + item.getBuyPrice(), NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                        }
                    })
                    .asGuiItem(event -> {
                        if (item.getSellPrice() != 0.0d && shop.getShopActions().get(ShopAction.SELL) == event.getClick()) {
                            if (Arrays.stream(player.getInventory().getContents()).noneMatch(it -> it.getType() == item.itemStack().getType())) {
                                player.sendMessage(ChatColor.RED + "You don't have any " + item.itemStack().getType() + " in your inventory");
                                return;
                            }

                            menuManager.openAmountSelectorForSell(item, player, shop, page);
                            return;
                        }

                        if (item.getBuyPrice() != 0.0d && shop.getShopActions().get(ShopAction.BUY) == event.getClick()) {
                            if (player.getInventory().firstEmpty() == -1) {
                                player.sendMessage(ChatColor.RED + "Your inventory is full!");
                                return;
                            }

                            if (shop.getEconomyProvider().has(player, item.getBuyPrice())) {
                                menuManager.openAmountSelectorForBuy(item, player, shop, page);
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have " + item.getBuyPrice() + " to buy " + item.itemStack().getType());
                            }
                        }
                    });

            gui.setItem(item.getSlot(), guiItem);
        }

        if (page != 1) {
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

        gui.setItem(6, 5, ItemBuilder.from(Material.BARRIER).asGuiItem(event -> menuManager.openMain(player)));
        gui.open(player);
    }

}
