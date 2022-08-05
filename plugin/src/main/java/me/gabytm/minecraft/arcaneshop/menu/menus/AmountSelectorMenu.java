package me.gabytm.minecraft.arcaneshop.menu.menus;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AmountSelectorMenu {

    private final MenuManager menuManager;

    public AmountSelectorMenu(@NotNull final MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void open(@NotNull final ShopItem item, @NotNull final Player player, @NotNull final Shop shop, final int page) {
        final Gui gui = Gui.gui()
                .title(Component.text("Amount selector", NamedTextColor.DARK_PURPLE))
                .rows(5)
                .disableAllInteractions()
                .create();

        final AtomicInteger amount = new AtomicInteger(item.item().getAmount());
        final GuiAction<InventoryClickEvent> buyAction = (event) -> {
            final double cost = amount.get() * item.getBuyPrice();

            if (shop.settings().getEconomyProvider().hasEnough(player, cost)) {
                player.sendMessage(ChatColor.GREEN + String.format("You have bought %dx %s for %.2f", amount.get(), item.item().getType(), cost));
                menuManager.openShop(player, shop, page);
            } else {
                player.sendMessage(ChatColor.RED + String.format("You can not afford to buy %dx %s for %.2f", amount.get(), item.item().getType(), cost));
            }
        };
        final Consumer<Integer> updateAmount = (amt) -> gui.updateItem(
                3, 5,
                ItemBuilder.from(item.item().clone())
                        .lore(lore -> {
                            lore.add(Component.empty());
                            lore.add(Component.text("Buy " + amt + " for " + String.format("%.2f", item.getBuyPrice() * amt), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                        })
                        .amount(amt)
                        .asGuiItem(buyAction)
        );

        updateAmount.accept(item.item().getAmount());

        //<editor-fold desc="Subtract">
        gui.setItem(
                2, 2,
                ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                        .name(Component.text("-1", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                        .asGuiItem(event -> {
                            if (amount.get() != 1) {
                                amount.set(Math.max(1, amount.get() - 1));
                                updateAmount.accept(amount.get());
                            }
                        })
        );
        gui.setItem(
                3, 2,
                ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                        .name(Component.text("-10", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                        .amount(10)
                        .asGuiItem(event -> {
                            if (amount.get() != 1) {
                                amount.set(Math.max(1, amount.get() - 10));
                                updateAmount.accept(amount.get());
                            }
                        })
        );
        gui.setItem(
                4, 2,
                ItemBuilder.from(Material.RED_STAINED_GLASS)
                        .name(Component.text("Set to 1", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                        .asGuiItem(event -> {
                            if (amount.get() != 1) {
                                amount.set(1);
                                updateAmount.accept(amount.get());
                            }
                        })
        );
        //</editor-fold>

        //<editor-fold desc="Add">
        gui.setItem(
                2, 8,
                ItemBuilder.from(Material.GREEN_STAINED_GLASS)
                        .name(Component.text("Set to " + item.item().getType().getMaxStackSize(), NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false))
                        .amount(item.item().getType().getMaxStackSize())
                        .asGuiItem(event -> {
                            if (amount.get() != item.item().getType().getMaxStackSize()) {
                                amount.set(item.item().getType().getMaxStackSize());
                                updateAmount.accept(amount.get());
                            }
                        })
        );
        gui.setItem(
                3, 8,
                ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE)
                        .name(Component.text("+10", NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false))
                        .amount(10)
                        .asGuiItem(event -> {
                            if (amount.get() != item.item().getType().getMaxStackSize()) {
                                amount.set(Math.min(item.item().getType().getMaxStackSize(), amount.get() + 10));
                                updateAmount.accept(amount.get());
                            }
                        })
        );
        gui.setItem(
                4, 8,
                ItemBuilder.from(Material.LIME_STAINED_GLASS_PANE)
                        .name(Component.text("+1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                        .asGuiItem(event -> {
                            if (amount.get() != item.item().getType().getMaxStackSize()) {
                                updateAmount.accept(amount.incrementAndGet());
                            }
                        })
        );
        //</editor-fold>

        gui.setItem(
                5, 5,
                ItemBuilder.skull()
                        .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4NTJiZjYxNmYzMWVkNjdjMzdkZTRiMGJhYTJjNWY4ZDhmY2E4MmU3MmRiY2FmY2JhNjY5NTZhODFjNCJ9fX0=")
                        .name(Component.text("Go back", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                        .asGuiItem(event -> menuManager.openShop(player, shop, page))
        );
        gui.open(player);
    }

}
