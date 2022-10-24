package me.gabytm.minecraft.arcaneshop.shop;

import com.google.common.collect.ImmutableMap;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
import me.gabytm.minecraft.arcaneshop.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
    private final CustomItemManager customItemManager;

    public ShopManagerImpl(@NotNull final File shopsFolderPath, @NotNull final CustomItemManager customItemManager) {
        this.shopsFolder = shopsFolderPath;
        this.customItemManager = customItemManager;
    }

    @Override
    public @Nullable Shop getShop(@NotNull final String name) {
        return shops.get(name.toLowerCase());
    }

    @Override
    public @NotNull Map<String, Shop> getShops() {
        return ImmutableMap.copyOf(shops);
    }

    @Override
    public boolean buyItem(@NotNull Shop shop, @NotNull ShopItem item, int amount, @NotNull Player player) {
        final double price = amount * item.getBuyPrice();
        final EconomyProvider economyProvider = shop.getEconomyProvider();

        if (!economyProvider.has(player, price)) {
            player.sendMessage(ChatColor.RED + String.format("You can not afford to buy %dx %s for %.2f", amount, item.getDisplayItem().getItemStack().getItemMeta().getDisplayName(), price));
            return false;
        }

        if (!economyProvider.subtract(player, price)) {
            player.sendMessage("Something went wrong while subtracting " + String.format("%.2f", price) + " from your account");
            return false;
        }

        if (item.isCommand()) {
            // TODO: 24/10/2022 replace papi placeholders 
            if (item.executeCommandsOnceForAllItems()) {
                item.getCommands().forEach(it -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it.replace("<amount>", String.valueOf(amount))));
            } else {
                for (int i = 0; i < amount; i++) {
                    item.getCommands().forEach(it -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it));
                }
            }

            player.sendMessage(ChatColor.GREEN + String.format("You have bought %dx %s for %.2f", amount, item.getDisplayItem().getItemStack().getType(), price));
            return true;
        }

        if (item.getItem() == null) {
            return false;
        }

        if (item.getItem().isCustom()) {
            customItemManager.getHandler(item.getItem().getCustomItemHandlerName()).giveItems(player, item.getItem().getCustomItemProperties(), amount);
        } else {
            final ItemStack itemStack = item.getItem().getItemStack().clone();
            itemStack.setAmount(amount);
            player.getInventory().addItem(itemStack);
        }

        player.sendMessage(ChatColor.GREEN + String.format("You have bought %dx %s for %.2f", amount, item.getDisplayItem().getItemStack().getType(), price));
        return true;
    }

    @Override
    public boolean sellItem(@NotNull Shop shop, @NotNull ShopItem item, int amount, @NotNull Player player) {
        // TODO: 24/10/2022 implement sell 
        return true;
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
