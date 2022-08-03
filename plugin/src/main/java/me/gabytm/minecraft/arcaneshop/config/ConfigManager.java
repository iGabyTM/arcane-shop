package me.gabytm.minecraft.arcaneshop.config;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private Config config;

    public ConfigManager(@NotNull final File dataFolder) {
        try {
            dataFolder.mkdirs();

            final Path path = dataFolder.toPath().resolve("config.yml");
            final YamlConfigurationLoader loader = createLoader(path);
            final CommentedConfigurationNode node = loader.load();

            this.config = node.get(Config.class);

            if (!Files.exists(path)) {
                Files.createFile(path);
                node.set(Config.class, this.config);
                loader.save(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private @NotNull YamlConfigurationLoader createLoader(@NotNull final Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .defaultOptions(options -> options.shouldCopyDefaults(true))
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
    }

    public Config getConfig() {
        return config;
    }

}
