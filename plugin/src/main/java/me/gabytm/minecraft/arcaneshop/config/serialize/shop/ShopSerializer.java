package me.gabytm.minecraft.arcaneshop.config.serialize.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.util.adventure.WrappedComponent;
import me.gabytm.minecraft.arcaneshop.config.configs.MainConfig;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import me.gabytm.minecraft.arcaneshop.shop.ShopImpl;
import me.gabytm.minecraft.arcaneshop.util.Enums;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.bukkit.event.inventory.ClickType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopSerializer implements TypeSerializer<Shop> {

    private final ItemCreator itemCreator;
    private final MainConfig mainConfig;
    private final EconomyManager economyManager;

    public ShopSerializer(@NotNull final ItemCreator itemCreator, MainConfig mainConfig, EconomyManager economyManager) {
        this.itemCreator = itemCreator;
        this.mainConfig = mainConfig;
        this.economyManager = economyManager;
    }

    @Override
    public Shop deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final DisplayItem mainMenuItem = itemCreator.createFromConfig(node.node("mainMenu", "item")).first();
        final int mainMenuSlot = node.node("mainMenu", "slot").getInt();


        final WrappedComponent menuTitle = node.node("menu", "title").get(WrappedComponent.class);
        int menuRows = node.node("menu", "rows").getInt(6);

        if (menuRows < 1 || menuRows > 6) {
            Logging.warning("Invalid number of rows {0}, the value should be between 1 and 6 rows, using 6 as default value", menuRows);
            menuRows = 6;
        }


        final List<ShopDecorationItem> decorationItems = new ArrayList<>();

        if (node.node("decorations").isMap()) {
            for (final ConfigurationNode decorationNode : node.node("decorations").childrenMap().values()) {
                final ShopDecorationItem shopDecorationItem = decorationNode.get(ShopDecorationItem.class);

                if (shopDecorationItem != null) {
                    decorationItems.add(shopDecorationItem);
                }
            }
        }

        final List<ShopItem> shopItems = new ArrayList<>();

        if (node.node("items").isMap()) {
            for (final ConfigurationNode shopItemNode : node.node("items").childrenMap().values()) {
                shopItems.add(shopItemNode.get(ShopItem.class));
            }
        } else {
            Logging.warning("No items in shop");
        }

        final String economyProviderName = node.node("economy").getString("");
        EconomyProvider economyProvider = economyManager.getProvider(economyProviderName);

        if (!economyProviderName.isEmpty() && economyProvider == null) {
            Logging.warning("Unknown economy provider {0}", economyProviderName);
        }

        if (economyProvider == null) {
            economyProvider = economyManager.getDefaultProvider();
        }

        final Map<ShopAction, ClickType> actions = new HashMap<>(ShopAction.values().length);

        if (node.node("shopActions").isMap()) {
            for (final ConfigurationNode value : node.node("shopActions").childrenMap().values()) {
                if (value.empty()) {
                    continue;
                }

                //noinspection ConstantConditions
                final ShopAction action = Enums.getOrNull(ShopAction.class, value.key().toString());

                if (action == null) {
                    Logging.warning("Unknown shop action '{0}", value.key());
                    continue;
                }

                final ClickType clickType = Enums.getOrNull(ClickType.class, value.getString());

                if (clickType == null) {
                    Logging.warning("Unknown click type '{0}", value.getString());
                    continue;
                }

                actions.put(action, clickType);
            }
        }

        // Add the missing ShopActions from the main config
        mainConfig.getShopActions().forEach(actions::putIfAbsent);
        return new ShopImpl(mainMenuItem.getItemStack(), mainMenuSlot, menuTitle, menuRows, shopItems, decorationItems, economyProvider, actions);
    }

    @Override
    public void serialize(Type type, @Nullable Shop obj, ConfigurationNode node) throws SerializationException {

    }

}
