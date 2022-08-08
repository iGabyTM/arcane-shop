package me.gabytm.minecraft.arcaneshop.config;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopSettings;
import me.gabytm.minecraft.arcaneshop.config.configs.ItemsConfig;
import me.gabytm.minecraft.arcaneshop.config.configs.MainConfig;
import me.gabytm.minecraft.arcaneshop.config.serialize.ComponentSerializer;
import me.gabytm.minecraft.arcaneshop.config.serialize.EconomyProviderSerializer;
import me.gabytm.minecraft.arcaneshop.config.serialize.item.DisplayItemSerializer;
import me.gabytm.minecraft.arcaneshop.config.serialize.item.ShopDecorationItemSerializer;
import me.gabytm.minecraft.arcaneshop.config.serialize.shop.ShopItemSerializer;
import me.gabytm.minecraft.arcaneshop.config.serialize.shop.ShopSerializer;
import me.gabytm.minecraft.arcaneshop.config.serialize.shop.ShopSettingsSerializer;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {

    private MainConfig mainConfig;
    private ItemsConfig itemsConfig;

    private final Path dataFolder;
    private final ItemCreator itemCreator;
    private final EconomyManager economyManager;

    public ConfigManager(
            @NotNull final Path dataFolder, @NotNull final ItemCreator itemCreator,
            @NotNull final EconomyManager economyManager
    ) {
        this.dataFolder = dataFolder;
        this.itemCreator = itemCreator;
        this.economyManager = economyManager;
    }

    private @Nullable <T> T load(@NotNull final Class<T> clazz, final Path pathToFile) {
        T config = null;

        try {
            final Path path = dataFolder.resolve(pathToFile);
            final YamlConfigurationLoader loader = createLoader(path);
            final CommentedConfigurationNode node = loader.load();

            config = node.get(clazz);

            if (!Files.exists(path)) {
                if (!Files.exists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.createFile(path);
                node.set(clazz, config);
                loader.save(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    public @NotNull YamlConfigurationLoader createLoader(@NotNull final Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options ->
                        options.shouldCopyDefaults(true)
                                .serializers(serializers ->
                                        serializers
                                                .register(Component.class, ComponentSerializer.INSTANCE)
                                                .register(DisplayItem.class, new DisplayItemSerializer(itemCreator))
                                                .register(EconomyProvider.class, new EconomyProviderSerializer(economyManager))
                                                // Shop
                                                .register(ShopDecorationItem.class, new ShopDecorationItemSerializer(itemsConfig))
                                                //.register(ShopSettings.class, new ShopSettingsSerializer(economyManager, mainConfig))
                                                .register(ShopItem.class, new ShopItemSerializer(itemCreator))
                                                .register(Shop.class, new ShopSerializer(itemCreator, mainConfig, economyManager))
                                )
                )
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public void loadMainConfig() {
        mainConfig = load(MainConfig.class, Paths.get("config.yml"));
    }

    public ItemsConfig getItemsConfig() {
        return itemsConfig;
    }

    public void loadItemsConfig() {
        itemsConfig = load(ItemsConfig.class, Paths.get("menus", "items.yml"));
    }

}
