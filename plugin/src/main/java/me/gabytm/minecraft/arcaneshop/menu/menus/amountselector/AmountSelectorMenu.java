package me.gabytm.minecraft.arcaneshop.menu.menus.amountselector;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
import me.gabytm.minecraft.arcaneshop.config.configs.AmountSelectorMenuConfig;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public class AmountSelectorMenu {

    private final MenuManager menuManager;
    private final ConfigManager configManager;
    private final ShopManager shopManager;

    public AmountSelectorMenu(
            @NotNull final MenuManager menuManager, @NotNull final ConfigManager configManager,
            @NotNull final ShopManager shopManager) {
        this.menuManager = menuManager;
        this.configManager = configManager;
        this.shopManager = shopManager;
    }

    /**
     * Create the action that is run after the amount is changed
     *
     * @param gui         an instance of the gui
     * @param config      the config of the menu
     * @param item        the item that is sold/bought
     * @param clickAction the action run to sell/buy the item
     * @param buy         whether the item is bought
     * @return an {@link IntConsumer} which accepts the current amount
     */
    private @NotNull IntConsumer createUpdateAmountAction(
            @NotNull final Gui gui, @NotNull final AmountSelectorMenuConfig.Item config,
            @NotNull final ShopItem item, @NotNull final GuiAction<InventoryClickEvent> clickAction,
            final boolean buy
    ) {
        return (amt) -> {
            final ItemStack itemStack = item.getDisplayItem().getItemStack();
            final GuiItem newGuiItem = ItemBuilder.from(itemStack.clone())
                    .lore(lore -> {
                        if (config.getLore().isEmpty()) {
                            return;
                        }

                        final double price = (buy ? item.getBuyPrice() : item.getSellPrice()) * amt;
                        final TagResolver amountPlaceholder = Placeholder.component("amount", Component.text(amt));
                        final TagResolver pricePlaceholder = Placeholder.component("price", Component.text(String.format("%.2f", price)));

                        config.getLore()
                                .stream()
                                .map(it -> MiniMessage.miniMessage().deserialize(it, amountPlaceholder, pricePlaceholder))
                                .map(ItemCreator::removeItalic)
                                .forEach(lore::add);
                    })
                    .amount(Math.min(amt, itemStack.getMaxStackSize()))
                    .asGuiItem(clickAction);

            // Set the new item in gui to update it visually
            gui.updateItem(config.getSlot(), newGuiItem);
        };
    }

    /**
     * Add all {@link AmountSelectorButton buttons} to the gui
     *
     * @param gui          an instance of the gui
     * @param config       the config of the menu
     * @param shopItem     the shop item that is sold/bought
     * @param amount       amount
     * @param updateAmount action to run after the amount was modified,
     *                     created by {@linkplain #createUpdateAmountAction(Gui, AmountSelectorMenuConfig.Item, ShopItem, GuiAction, boolean)}
     * @param buy          whether the item is bought
     */
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
                    new GuiItem(button.getDisplayItem().getItemStack(), event -> {
                        final int currentAmount = amount.get();
                        final ItemStack itemStack = shopItem.getItem().getItemStack();
                        final int value = button.valueIsMax() ? itemStack.getMaxStackSize() : button.getValue();

                        switch (button.getAction()) {
                            case ADD: {
                                amount.addAndGet(value);
                                break;
                            }

                            case SET: {
                                amount.set(value);
                                break;
                            }

                            case SUBTRACT: {
                                // Subtract the 'value' from the current amount only if the current amount is more than 1
                                if (amount.get() > 1) {
                                    amount.set(Math.max(1, amount.get() - value));
                                }

                                break;
                            }
                        }

                        // Update the item only if the amount was changed
                        if (amount.get() != currentAmount) {
                            updateAmount.accept(amount.get());
                        }
                    })
            );
        }
    }

    public void open(
            @NotNull final ShopItem item, @NotNull final Player player,
            @NotNull final Shop shop, final int page,
            final boolean buy
    ) {
        final AmountSelectorMenuConfig config = buy ? configManager.getBuyAmountSelectorMenuConfig() : configManager.getSellAmountSelectorMenuConfig();
        final Gui gui = Gui.gui()
                .title(config.getTitle())
                .rows(config.getRows())
                .disableAllInteractions()
                .create();

        final AtomicInteger amount = new AtomicInteger(item.getAmount());

        final GuiAction<InventoryClickEvent> clickAction = (event) -> {
            if (buy) {
                if (shopManager.buyItem(shop, item, amount.get(), player).success()) {
                    menuManager.openShop(player, shop, page);
                }
            } else {
                if (shopManager.sellItem(shop, item, amount.get(), player).success()) {
                    menuManager.openShop(player, shop, page);
                }
            }
        };
        final IntConsumer updateAmount = createUpdateAmountAction(gui, config.item(), item, clickAction, buy);
        updateAmount.accept(item.getAmount()); // Set the current amount

        addButtons(gui, config, item, amount, updateAmount, buy);

        // TODO: 24/10/2022 remove this hardcoded item
        gui.setItem(
                5, 5,
                ItemBuilder.skull()
                        .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4NTJiZjYxNmYzMWVkNjdjMzdkZTRiMGJhYTJjNWY4ZDhmY2E4MmU3MmRiY2FmY2JhNjY5NTZhODFjNCJ9fX0=")
                        .name(ItemCreator.removeItalic(Component.text("Go back", NamedTextColor.RED)))
                        .asGuiItem(event -> menuManager.openShop(player, shop, page))
        );
        gui.open(player);
    }

}
