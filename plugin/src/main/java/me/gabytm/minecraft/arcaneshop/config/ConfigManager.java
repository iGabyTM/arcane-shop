package me.gabytm.minecraft.arcaneshop.config;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.config.serialize.ComponentSerializer;
import me.gabytm.minecraft.arcaneshop.config.serialize.DisplayItemSerializer;
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

    private Config config;
    private final Path dataFolder;
    private final ItemCreator itemCreator;

    public ConfigManager(@NotNull final Path dataFolder, @NotNull final ItemCreator itemCreator) {
        this.dataFolder = dataFolder;
        this.itemCreator = itemCreator;

        config = load(Config.class, Paths.get("config.yml"));
        //load(ItemsConfig.class, Paths.get("menus", "items.yml"));
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

    private @NotNull YamlConfigurationLoader createLoader(@NotNull final Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options ->
                        options.shouldCopyDefaults(true)
                                .serializers(serializers ->
                                        serializers.register(Component.class, ComponentSerializer.INSTANCE)
                                                .register(DisplayItem.class, new DisplayItemSerializer(itemCreator))
                                )
                )
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
    }

    public Config getConfig() {
        return config;
    }

}
