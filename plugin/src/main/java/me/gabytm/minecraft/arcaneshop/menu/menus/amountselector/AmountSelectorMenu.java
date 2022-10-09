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
import me.gabytm.minecraft.arcaneshop.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public class AmountSelectorMenu {

    //TODO move this
    private final BukkitAudiences audiences = BukkitAudiences.create(JavaPlugin.getProvidingPlugin(ArcaneShop.class));
    private final MenuManager menuManager;
    private final ConfigManager configManager;
    private final CustomItemManager customItemManager = new CustomItemManager();

    private void sell(@NotNull final Player player, @NotNull final ShopItem item, final int amount) {
        //player.getInventory().remove();
    }

    public AmountSelectorMenu(@NotNull final MenuManager menuManager, @NotNull final ConfigManager configManager) {
        this.menuManager = menuManager;
        this.configManager = configManager;
    }

    private @NotNull IntConsumer createUpdateAmountAction(
            @NotNull final Gui gui, @NotNull final AmountSelectorMenuConfig.Item config,
            @NotNull final ShopItem item, @NotNull final AtomicInteger amount,
            @NotNull final GuiAction<InventoryClickEvent> clickAction, final boolean buy
    ) {
        return (amt) -> gui.updateItem(
                config.getSlot(),
                ItemBuilder.from(item.displayItem().item().clone())
                        .lore(lore -> {
                            if (config.getLore().isEmpty()) {
                                return;
                            }

                            final double price = (buy ? item.getBuyPrice() : item.getSellPrice()) * amt;
                            final TagResolver amountPlaceholder = Placeholder.component("amount", Component.text(amt));
                            final TagResolver pricePlaceholder = Placeholder.component("price", Component.text(String.format("%.2f", price)));

                            config.getLore().stream()
                                    .map(it -> MiniMessage.miniMessage().deserialize(it, amountPlaceholder, pricePlaceholder))
                                    .map(it -> Component.empty().decoration(TextDecoration.ITALIC, false).append(it))
                                    .forEach(lore::add);
                        })
                        .amount(Math.min(amt, item.displayItem().item().getMaxStackSize()))
                        .asGuiItem(clickAction)
        );
    }

    private void addButtons(
            @NotNull final Gui gui, @NotNull final AmountSelectorMenuConfig config,
            @NotNull final ShopItem shopItem, @NotNull final AtomicInteger amount,
            @NotNull final IntConsumer updateAmount, final boolean buy
    ) {
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
                        final ItemStack itemStack = shopItem.getItem().item();

                        switch (button.getAction()) {
                            case ADD: {
                                if (amount.get() != itemStack.getMaxStackSize()) {
                                    amount.set(Math.min(itemStack.getMaxStackSize(), amount.get() + button.getValue()));
                                }

                                break;
                            }

                            case SET: {
                                if (button.getValue() == -1) {
                                    amount.set(itemStack.getMaxStackSize());
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
    }

    public void open(@NotNull final ShopItem item, @NotNull final Player player, @NotNull final Shop shop, final int page, final boolean buy) {
        final AmountSelectorMenuConfig config = buy ? configManager.getBuyAmountSelectorMenuConfig() : configManager.getSellAmountSelectorMenuConfig();
        final Gui gui = Gui.gui()
                .title(config.getTitle())
                .rows(config.getRows())
                .disableAllInteractions()
                .create();

        final AtomicInteger amount = new AtomicInteger(item.getAmount());

        final GuiAction<InventoryClickEvent> clickAction = (event) -> {
            final double cost = amount.get() * (buy ? item.getBuyPrice() : item.getSellPrice());

            if (buy) {
                if (shop.getEconomyProvider().has(player, cost)) {
                    if (!shop.getEconomyProvider().subtract(player, cost)) {
                        player.sendMessage("Something went wrong while subtracting " + String.format("%.2f", cost) + " from your account");
                        menuManager.openShop(player, shop, page);
                    }

                    final String sNbt = new NBTItem(item.displayItem().item()).toString();
                    //noinspection PatternValidation
                    final Key key = Key.key(Key.MINECRAFT_NAMESPACE, item.displayItem().item().getType().getKey().getKey());
                    final BinaryTagHolder binaryTagHolder = BinaryTagHolder.binaryTagHolder(sNbt);
                    final HoverEvent<HoverEvent.ShowItem> hover = HoverEvent.showItem(key, amount.get(), binaryTagHolder);

                    final Component message = Component.text("You have bought " + amount.get() + "x ", NamedTextColor.GREEN)
                            .append(MiniMessage.miniMessage().deserialize(item.displayItem().name() + " (hover)").hoverEvent(hover))
                            .append(Component.text(" for " + String.format("%.2f", cost)));
                    audiences.player(player).sendMessage(message);

                    if (item.getItem().isCustom()) {
                        customItemManager.getHandler(item.getItem().getCustomItemHandlerName()).giveItems(player, item.getItem().getCustomItemProperties(), amount.get());
                    } else {
                        final ItemStack itemStack = item.getItem().item().clone();
                        itemStack.setAmount(amount.get());
                        player.getInventory().addItem(itemStack);
                    }

                    menuManager.openShop(player, shop, page);
                    return;
                }

                player.sendMessage(ChatColor.RED + String.format("You can not afford to buy %dx %s for %.2f", amount.get(), item.displayItem().item().getItemMeta().getDisplayName(), cost));
                return;
            }

            final int itemsTaken = customItemManager.getHandler(item.getItem().getCustomItemHandlerName()).takeItems(player, item.getItem().getCustomItemProperties(), amount.get());

            if (itemsTaken == 0) {
                player.sendMessage(ChatColor.RED + "You don't have any " + item.displayItem().item().getItemMeta().getDisplayName() + " in your inventory!");
            } else if (itemsTaken == amount.get()) {
                shop.getEconomyProvider().add(player, cost);
                player.sendMessage(ChatColor.GREEN + String.format("You sold %dx %s for %.2f", itemsTaken, item.displayItem().item().getItemMeta().getDisplayName(), cost));
            } else {
                final double moneyToGive = itemsTaken * item.getSellPrice();
                shop.getEconomyProvider().add(player, moneyToGive);
                player.sendMessage(ChatColor.YELLOW + String.format("You sold only %dx %s for %.2f", itemsTaken, item.displayItem().item().getItemMeta().getDisplayName(), moneyToGive));
            }

            menuManager.openShop(player, shop, page);
        };
        final IntConsumer updateAmount = createUpdateAmountAction(gui, config.item(), item, amount, clickAction, buy);

        updateAmount.accept(item.getAmount());
        addButtons(gui, config, item, amount, updateAmount, buy);

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
