package me.gabytm.minecraft.arcaneshop.menu.menus.amountselector;

import de.tr7zw.nbtapi.NBTItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.gabytm.minecraft.arcaneshop.ArcaneShop;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
import me.gabytm.minecraft.arcaneshop.config.configs.AmountSelectorMenuConfig;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AmountSelectorMenu {

    //TODO move this
    private final BukkitAudiences audiences = BukkitAudiences.create(JavaPlugin.getProvidingPlugin(ArcaneShop.class));
    private final MenuManager menuManager;
    private final ConfigManager configManager;

    public AmountSelectorMenu(@NotNull final MenuManager menuManager, @NotNull final ConfigManager configManager) {
        this.menuManager = menuManager;
        this.configManager = configManager;
    }

    public void open(@NotNull final ShopItem item, @NotNull final Player player, @NotNull final Shop shop, final int page, final boolean buy) {
        final AmountSelectorMenuConfig config = buy ? configManager.getBuyAmountSelectorMenuConfig() : configManager.getSellAmountSelectorMenuConfig();
        final Gui gui = Gui.gui()
                .title(config.getTitle())
                .rows(config.getRows())
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
                config.getItemSlot(),
                ItemBuilder.from(item.displayItem().item().clone())
                        .lore(lore -> {
                            lore.add(Component.empty());

                            if (buy) {
                                lore.add(Component.text("Buy " + amt + " for $" + String.format("%.2f", item.getBuyPrice() * amt), NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
                            } else {
                                lore.add(Component.text("Sell " + amt + " for $" + String.format("%.2f", item.getBuyPrice() * amt), NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
                            }
                        })
                        .amount(amt)
                        .asGuiItem(buyAction)
        );

        updateAmount.accept(item.itemStack().getAmount());

        for (final Map.Entry<String, AmountSelectorButton> entry : config.getButtons().entrySet()) {
            final AmountSelectorButton button = entry.getValue();

            if (button == null) {
                continue;
            }

            if (button.getSlot() >= config.getRows() * 9) {
                Logging.warning(
                        "Could not set button {0} in slot {1} ({2} selector menu), only {3} slots available",
                        entry.getKey(), button.getSlot(), (buy ? "buy" : "sell"), config.getRows() * 9 - 1
                );
                continue;
            }

            gui.setItem(
                    button.getSlot(),
                    new GuiItem(button.getDisplayItem().item(), event -> {
                        final int currentAmount = amount.get();

                        switch (button.getAction()) {
                            case ADD: {
                                if (amount.get() != item.itemStack().getType().getMaxStackSize()) {
                                    amount.set(Math.min(item.itemStack().getType().getMaxStackSize(), amount.get() + button.getValue()));
                                }

                                break;
                            }

                            case SET: {
                                if (button.getValue() == -1) {
                                    amount.set(item.itemStack().getType().getMaxStackSize());
                                } else {
                                    amount.set(Math.max(1, Math.min(button.getValue(), 64)));
                                }

                                break;
                            }

                            case SUBTRACT: {
                                if (amount.get() != 1) {
                                    amount.set(Math.max(1, amount.get() - button.getValue()));
                                }

                                break;
                            }
                        }

                        if (amount.get() != currentAmount) {
                            updateAmount.accept(amount.get());
                        }
                    })
            );
        }

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
