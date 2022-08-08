package me.gabytm.minecraft.arcaneshop.shop;

import com.google.common.collect.ImmutableMap;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShopManagerImpl implements ShopManager {

    private final Map<String, Shop> shops = new HashMap<>();

    private final File shopsFolder;

    public ShopManagerImpl(File shopsFolderPath) {
        this.shopsFolder = shopsFolderPath;
    }

    /*private final EconomyManager economyManager;
    private final ConfigManager configManager;

    public ShopManagerImpl(@NotNull final EconomyManager economyManager, @NotNull final ConfigManager configManager) {
        this.economyManager = economyManager;
        this.configManager = configManager;*/

        /*final ShopSettings settings = new ShopSettingsImpl(
                economyManager.getProvider("vault"),
                ImmutableMap.<ShopAction, ClickType>builder()
                        .put(ShopAction.SELL, ClickType.LEFT)
                        .put(ShopAction.SELL_ALL, ClickType.SHIFT_LEFT)
                        .put(ShopAction.BUY, ClickType.RIGHT)
                        .put(ShopAction.BUY_MULTIPLE, ClickType.SHIFT_RIGHT)
                        .build()
        );

        final Shop blocks = new ShopImpl(
                new ItemStack(Material.BRICKS),
                Component.text("Blocks"),
                0,
                Arrays.asList(
                        new ShopItemImpl(
                                new DisplayItemImpl(new ItemStack(Material.STONE), "", Collections.emptyList()),
                                new ItemStack(Material.STONE),
                                0,
                                1,
                                1.0,
                                0.5
                        ),
                        new ShopItemImpl(
                                new DisplayItemImpl(new ItemStack(Material.DIRT), "", Collections.emptyList()),
                                new ItemStack(Material.DIRT),
                                1,
                                1,
                                1.0,
                                0.5
                        ),
                        new ShopItemImpl(
                                new DisplayItemImpl(new ItemStack(Material.BRICKS), "", Collections.emptyList()),
                                new ItemStack(Material.BRICKS),
                                2,
                                2,
                                5,
                                2
                        ),
                        new ShopItemImpl(
                                new DisplayItemImpl(new ItemStack(Material.OBSIDIAN), "", Collections.emptyList()),
                                new ItemStack(Material.OBSIDIAN),
                                3,
                                3,
                                5,
                                2
                        )
                ),
                settings
        );

        final Shop ores = new ShopImpl(
                new ItemStack(Material.DIAMOND),
                Component.text("Ores"),
                1,
                Arrays.asList(
                        new ShopItemImpl(
                                new DisplayItemImpl(new ItemStack(Material.DIAMOND), "", Collections.emptyList()),
                                new ItemStack(Material.DIAMOND),
                                0,
                                1,
                                100,
                                25
                        ),
                        new ShopItemImpl(
                                new DisplayItemImpl(new ItemStack(Material.EMERALD), "", Collections.emptyList()),
                                new ItemStack(Material.EMERALD),
                                1,
                                1,
                                100,
                                25
                        )
                ),
                settings
        );

        shops.put("blocks", blocks);
        shops.put("ores", ores);*/
    //}

    @Override
    public @Nullable Shop getShop(@NotNull final String name) {
        return shops.get(name.toLowerCase());
    }

    @Override
    public @NotNull Map<String, Shop> getShops() {
        return ImmutableMap.copyOf(shops);
    }

    public void loadShops(@NotNull final ConfigManager configManager) {
        try {
            shops.clear();
            shopsFolder.mkdirs();
            final File[] files = shopsFolder.listFiles(it -> it.getName().endsWith(".yml"));

            if (files == null) {
                Logging.warning("No shops found on {0}", shopsFolder.getAbsolutePath());
                return;
            }

            for (final File shopFile : files) {
                final YamlConfigurationLoader loader = configManager.createLoader(shopFile.toPath());
                final Shop shop =  loader.load().get(Shop.class);
                shops.put(shopFile.getName().replace(".yml", ""), shop);
            }

            Logging.warning("Loaded {0} shops: {1}", shops.size(), String.join(", ", shops.keySet()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
