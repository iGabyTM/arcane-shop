package me.gabytm.minecraft.arcaneshop.config.serialize.shop;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopSettings;
import me.gabytm.minecraft.arcaneshop.config.configs.MainConfig;
import me.gabytm.minecraft.arcaneshop.shop.ShopSettingsImpl;
import me.gabytm.minecraft.arcaneshop.util.Enums;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.bukkit.event.inventory.ClickType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ShopSettingsSerializer /*implements TypeSerializer<ShopSettings>*/ {

    /*private final EconomyManager economyManager;
    private final MainConfig mainConfig;

    public ShopSettingsSerializer(@NotNull final EconomyManager economyManager, @NotNull final MainConfig configManager) {
        this.economyManager = economyManager;
        this.mainConfig = configManager;
    }

    @Override
    public ShopSettings deserialize(Type type, ConfigurationNode node) throws SerializationException {
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
        return new ShopSettingsImpl(economyProvider, actions);
    }

    @Override
    public void serialize(Type type, @Nullable ShopSettings obj, ConfigurationNode node) throws SerializationException {

    }*/

}
