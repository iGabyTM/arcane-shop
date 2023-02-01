package me.gabytm.minecraft.arcaneshop.menu.menus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.gabytm.minecraft.arcaneshop.api.ArcaneShopAPI;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.price.PriceModifier;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ShopMenu {

    private final MenuManager menuManager;
    private final ArcaneShopAPI api;

    public ShopMenu(@NotNull final MenuManager menuManager, @NotNull final ArcaneShopAPI api) {
        this.menuManager = menuManager;
        this.api = api;
    }

    public void open(@NotNull final Player player, @NotNull final Shop shop, final int page) {
        final List<ShopItem> shopItems = shop.getItems()
                .stream()
                .filter(shopItem -> shopItem.getPage() == page)
                .collect(Collectors.toList());

        if (page != 1 && shopItems.isEmpty()) {
            return;
        }

        final Gui gui = Gui.gui()
                .title(shop.getMenuTitle().component())
                .rows(shop.getMenuRows())
                .disableAllInteractions()
                .create();

        shop.getDecorations()
                .stream()
                .filter(decorationItem -> decorationItem.getPage() == ShopDecorationItem.ALL_PAGES || decorationItem.getPage() == page)
                .forEach(decorationItem -> gui.setItem(decorationItem.getSlots(), new GuiItem(decorationItem.getDisplayItem().getItemStack())));

        final List<PriceModifier> priceModifiers = api.getPriceModifierManager().getPlayerModifiers(player, shop);

        for (final ShopItem shopItem : shopItems) {
            final GuiItem guiItem = ItemBuilder.from(shopItem.getDisplayItem().getItemStack().clone())
                    .amount(shopItem.getAmount())
                    .lore(lore -> {
                        if (shopItem.canBeSold()) {
                            final Optional<PriceModifier> optionalPriceModifier = priceModifiers.stream()
                                    .filter(modifier -> modifier.getItems().isEmpty() || modifier.getItems().contains(shopItem.getId()))
                                    .filter(modifier -> modifier.getAction() == PriceModifier.Action.BOTH || modifier.getAction() == PriceModifier.Action.SELL)
                                    .filter(modifier -> player.hasPermission("arcaneshop.modifier." + modifier.getName()))
                                    .max(Comparator.comparingDouble(PriceModifier::getValue));

                            if (optionalPriceModifier.isPresent()) {
                                lore.add(ItemCreator.removeItalic(MiniMessage.miniMessage().deserialize(String.format("<green>Sell for: <gray><st>$%.2f</st></gray> <green>$%.2f (+%.1f%%)", shopItem.getSellPrice(), shopItem.getSellPrice() + (optionalPriceModifier.get().getValue() / 100 * shopItem.getSellPrice()), optionalPriceModifier.get().getValue()))));
                            } else {
                                lore.add(ItemCreator.removeItalic(Component.text("Sell for: $" + shopItem.getSellPrice(), NamedTextColor.GREEN)));
                            }
                        }

                        if (shopItem.canBeBought()) {
                            final Optional<PriceModifier> optionalPriceModifier = priceModifiers.stream()
                                    .filter(modifier -> modifier.getItems().isEmpty() || modifier.getItems().contains(shopItem.getId()))
                                    .filter(modifier -> modifier.getAction() == PriceModifier.Action.BOTH || modifier.getAction() == PriceModifier.Action.BUY)
                                    .filter(modifier -> player.hasPermission("arcaneshop.modifier." + modifier.getName()))
                                    .max(Comparator.comparingDouble(PriceModifier::getValue));

                            if (optionalPriceModifier.isPresent()) {
                                lore.add(ItemCreator.removeItalic(MiniMessage.miniMessage().deserialize(String.format("<red>Buy for: <gray><st>$%.2f</st></gray> <red>$%.2f (-%.1f%%)", shopItem.getSellPrice(), shopItem.getSellPrice() - (optionalPriceModifier.get().getValue() / 100 * shopItem.getSellPrice()), optionalPriceModifier.get().getValue()))));
                            } else {
                                lore.add(ItemCreator.removeItalic(Component.text("Buy for: $" + shopItem.getBuyPrice(), NamedTextColor.RED)));
                            }
                        }
                    })
                    .asGuiItem(event -> {
                        final String displayName = shopItem.getDisplayItem().getItemStack().getItemMeta().getDisplayName();

                        if (shop.getShopActions().get(ShopAction.SELL) == event.getClick()) {
                            // The item can not be sold
                            if (!shopItem.canBeSold()) {
                                player.sendMessage(ChatColor.RED + "Item " + displayName + " can not be sold");
                                return;
                            }

                            // The item is custom, open the menu and let the custom item handler to check if they have enough items
                            if (shopItem.getItem() != null && shopItem.getItem().isCustom()) {
                                menuManager.openAmountSelectorForSell(shopItem, player, shop, page);
                                return;
                            }

                            // Check if the player has any item of the same type in their inventory
                            final boolean hasItems = Arrays.stream(player.getInventory().getContents())
                                    .filter(Objects::nonNull)
                                    .anyMatch(item -> shopItem.getItem() != null && item.getType() == shopItem.getItem().getItemStack().getType());

                            if (!hasItems) {
                                player.sendMessage(ChatColor.RED + "You don't have any " + displayName + " in your inventory");
                                return;
                            }

                            menuManager.openAmountSelectorForSell(shopItem, player, shop, page);
                            return;
                        }

                        if (shop.getShopActions().get(ShopAction.BUY) == event.getClick()) {
                            // The item can not be bought
                            if (!shopItem.canBeBought()) {
                                player.sendMessage(ChatColor.RED + "Item " + displayName + " can not be bought.");
                                return;
                            }

                            // Player's inventory is full
                            if (player.getInventory().firstEmpty() == -1) {
                                player.sendMessage(ChatColor.RED + "Your inventory is full!");
                                return;
                            }

                            // The player can afford to buy at least one item
                            if (shop.getEconomyProvider().has(player, shopItem.getBuyPrice())) {
                                menuManager.openAmountSelectorForBuy(shopItem, player, shop, page);
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have " + shopItem.getBuyPrice() + " to buy " + displayName);
                            }
                        }
                    });

            gui.setItem(shopItem.getSlot(), guiItem);
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
