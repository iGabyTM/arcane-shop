package me.gabytm.minecraft.arcaneshop.config.serialize.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopSettings;
import me.gabytm.minecraft.arcaneshop.config.configs.MainConfig;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import me.gabytm.minecraft.arcaneshop.shop.ShopImpl;
import me.gabytm.minecraft.arcaneshop.shop.ShopSettingsImpl;
import me.gabytm.minecraft.arcaneshop.util.Enums;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import net.kyori.adventure.text.Component;
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
import java.util.function.Supplier;

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
        final DisplayItem item = itemCreator.createFromConfig(node.node("displayItem"));
        System.out.println("item = " + item);
        final Component title = node.node("title").get(Component.class, Component.empty());
        System.out.println("title = " + title);
        final int slot = node.node("slot").getInt();
        System.out.println("slot = " + slot);

        final List<ShopDecorationItem> decorationItems = new ArrayList<>();

        if (node.node("decorations").isMap()) {
            for (final ConfigurationNode decorationNode : node.node("decorations").childrenMap().values()) {
                final ShopDecorationItem shopDecorationItem = decorationNode.get(ShopDecorationItem.class);

                if (shopDecorationItem != null) {
                    decorationItems.add(shopDecorationItem);
                }
            }
        }
        System.out.println("decorationItems = " + decorationItems);

        final List<ShopItem> shopItems = new ArrayList<>();

        if (node.node("items").isMap()) {
            for (final ConfigurationNode shopItemNode : node.node("items").childrenMap().values()) {
                shopItems.add(shopItemNode.get(ShopItem.class));
            }
        } else {
            Logging.warning("No items in shop");
        }

        System.out.println("shopItems = " + shopItems);
        /*final ShopSettings settings = node.node("settings").get(
                ShopSettings.class,
                (Supplier<ShopSettings>) () -> new ShopSettingsImpl(economyManager.getDefaultProvider(), mainConfig.getShopActions())
        );
        System.out.println("settings = " + settings);*/

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

        mainConfig.getShopActions().forEach(actions::putIfAbsent);

        return new ShopImpl(item.item(), title, slot, shopItems, decorationItems, economyProvider, actions);
    }

    @Override
    public void serialize(Type type, @Nullable Shop obj, ConfigurationNode node) throws SerializationException {

    }

}
