package me.gabytm.minecraft.arcaneshop.menu.menus;

import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import me.gabytm.minecraft.arcaneshop.ArcaneShop;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AmountSelectorMenu {

    //TODO move this
    private final BukkitAudiences audiences = BukkitAudiences.create(JavaPlugin.getProvidingPlugin(ArcaneShop.class));
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

        final AtomicInteger amount = new AtomicInteger(item.itemStack().getAmount());
        final GuiAction<InventoryClickEvent> buyAction = (event) -> {
            final double cost = amount.get() * item.getBuyPrice();

            if (shop.getEconomyProvider().has(player, cost)) {
                final String sNbt = new NBTItem(item.displayItem().item()).toString();
                //noinspection PatternValidation
                final Key key = Key.key(Key.MINECRAFT_NAMESPACE, item.displayItem().item().getType().getKey().getKey());
                final BinaryTagHolder binaryTagHolder = BinaryTagHolder.binaryTagHolder(sNbt);
                final HoverEvent<HoverEvent.ShowItem> hover = HoverEvent.showItem(key, amount.get(), binaryTagHolder);

                final Component message = Component.text("You have bought " + amount.get() + "x [", NamedTextColor.GREEN)
                        .append(Component.text(item.displayItem().name()).hoverEvent(hover))
                        .append(Component.text("] for " + String.format("%.2f", cost)));
                audiences.player(player).sendMessage(message);
                menuManager.openShop(player, shop, page);
            } else {
                player.sendMessage(ChatColor.RED + String.format("You can not afford to buy %dx %s for %.2f", amount.get(), item.itemStack().getType(), cost));
            }
        };
        final Consumer<Integer> updateAmount = (amt) -> gui.updateItem(
                3, 5,
                ItemBuilder.from(item.displayItem().item().clone())
                        .lore(lore -> {
                            lore.add(Component.empty());
                            lore.add(Component.text("Buy " + amt + " for " + String.format("%.2f", item.getBuyPrice() * amt), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                        })
                        .amount(amt)
                        .asGuiItem(buyAction)
        );

        updateAmount.accept(item.itemStack().getAmount());

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
                        .name(Component.text("Set to " + item.itemStack().getType().getMaxStackSize(), NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false))
                        .amount(item.itemStack().getType().getMaxStackSize())
                        .asGuiItem(event -> {
                            if (amount.get() != item.itemStack().getType().getMaxStackSize()) {
                                amount.set(item.itemStack().getType().getMaxStackSize());
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
                            if (amount.get() != item.itemStack().getType().getMaxStackSize()) {
                                amount.set(Math.min(item.itemStack().getType().getMaxStackSize(), amount.get() + 10));
                                updateAmount.accept(amount.get());
                            }
                        })
        );
        gui.setItem(
                4, 8,
                ItemBuilder.from(Material.LIME_STAINED_GLASS_PANE)
                        .name(Component.text("+1", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                        .asGuiItem(event -> {
                            if (amount.get() != item.itemStack().getType().getMaxStackSize()) {
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
