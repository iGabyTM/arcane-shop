package me.gabytm.minecraft.arcaneshop.shop;

import com.google.common.collect.ImmutableMap;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopResult;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
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
    public @NotNull ShopResult buyItem(@NotNull Shop shop, @NotNull ShopItem item, int amount, @NotNull Player player) {
        final double price = amount * item.getBuyPrice();
        final EconomyProvider economyProvider = shop.getEconomyProvider();

        if (!economyProvider.has(player, price)) {
            player.sendMessage(ChatColor.RED + String.format("You can not afford to buy %dx %s for %.2f", amount, item.getDisplayItem().getItemStack().getItemMeta().getDisplayName(), price));
            return ShopResultImpl.buy(ShopResult.Result.NOT_ENOUGH_MONEY, shop, item, amount, price);
        }

        if (!economyProvider.subtract(player, price)) {
            player.sendMessage("Something went wrong while subtracting " + String.format("%.2f", price) + " from your account");
            return ShopResultImpl.buy(ShopResult.Result.COULD_NOT_SUBTRACT_MONEY, shop, item, amount, price);
        }

        if (item.isCommand()) {
            // TODO: 24/10/2022 replace papi placeholders 
            if (item.executeCommandsOnceForAllItems()) {
                item.getCommands()
                        .forEach(it ->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it.replace("<amount>", String.valueOf(amount))));
            } else {
                for (int i = 0; i < amount; i++) {
                    item.getCommands()
                            .forEach(it ->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it));
                }
            }

            player.sendMessage(ChatColor.GREEN + String.format("You have bought %dx %s for %.2f", amount, item.getDisplayItem().getItemStack().getType(), price));
            return ShopResultImpl.buy(ShopResult.Result.SUCCESS, shop, item, amount, price);
        }

        if (item.getItem() == null) {
            return ShopResultImpl.buy(ShopResult.Result.ITEM_IS_NULL, shop, item, 0, 0);
        }

        if (item.getItem().isCustom()) {
            customItemManager.getHandler(item.getItem().getCustomItemHandlerName()).giveItems(player, item.getItem().getCustomItemProperties(), amount);
        } else {
            final ItemStack itemStack = item.getItem().getItemStack().clone();
            itemStack.setAmount(amount);
            player.getInventory().addItem(itemStack);
        }

        player.sendMessage(ChatColor.GREEN + String.format("You have bought %dx %s for %.2f", amount, item.getDisplayItem().getItemStack().getType(), price));
        return ShopResultImpl.buy(ShopResult.Result.SUCCESS, shop, item, amount, price);
    }

    @Override
    public @NotNull ShopResult sellItem(@NotNull Shop shop, @NotNull ShopItem item, int amount, @NotNull Player player) {
        if (item.isCommand()) {
            return ShopResultImpl.buy(ShopResult.Result.ITEM_IS_COMMAND, shop, item, 0, 0);
        }

        if (item.getItem() == null) {
            return ShopResultImpl.buy(ShopResult.Result.ITEM_IS_NULL, shop, item, 0, 0);
        }

        if (item.getItem().isCustom()) {
            final int itemsTaken = customItemManager.getHandler(item.getItem().getCustomItemHandlerName()).takeItems(player, item.getItem().getCustomItemProperties(), amount);

            if (itemsTaken == 0) {
                player.sendMessage(ChatColor.RED + "You don't have any " + item.getDisplayItem().getItemStack().getItemMeta().getDisplayName() + " in your inventory!");
                return ShopResultImpl.sell(ShopResult.Result.NO_ITEMS_IN_INVENTORY, shop, item, 0, 0);
            }

            if (itemsTaken == amount) {
                final double moneyToGive = itemsTaken * amount;
                shop.getEconomyProvider().add(player, moneyToGive);
                player.sendMessage(ChatColor.GREEN + String.format("You sold %dx %s for %.2f", itemsTaken, item.getDisplayItem().getItemStack().getItemMeta().getDisplayName(), moneyToGive));
                return ShopResultImpl.sell(ShopResult.Result.SUCCESS, shop, item, amount, moneyToGive);
            }

            final double moneyToGive = itemsTaken * item.getSellPrice();
            shop.getEconomyProvider().add(player, moneyToGive);
            player.sendMessage(ChatColor.YELLOW + String.format("You sold only %dx %s for %.2f", itemsTaken, item.getDisplayItem().getItemStack().getItemMeta().getDisplayName(), moneyToGive));
            return ShopResultImpl.sell(ShopResult.Result.SUCCESS, shop, item, itemsTaken, moneyToGive);
        }

        // TODO: 03/11/2022 add sell logic for normal items 
        return ShopResultImpl.sell(ShopResult.Result.SUCCESS, shop, item, 0, 0);
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
                final String shopName = shopFile.getName().replace(".yml", "");
                final Shop shop =  loader.load().get(Shop.class);

                if (shop == null) {
                    System.out.println("Could not load shop from " + shopFile.getAbsolutePath());
                    continue;
                }

                shop.setName(shopName);
                shops.put(shopName, shop);
            }

            Logging.warning("Loaded {0} shops: {1}", shops.size(), String.join(", ", shops.keySet()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
