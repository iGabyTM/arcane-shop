package me.gabytm.minecraft.arcaneshop;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import me.gabytm.minecraft.arcaneshop.api.ArcaneShopAPI;
import me.gabytm.minecraft.arcaneshop.api.ArcaneShopAPIImpl;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import me.gabytm.minecraft.arcaneshop.commands.ReloadCommand;
import me.gabytm.minecraft.arcaneshop.commands.ShopCommand;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
import me.gabytm.minecraft.arcaneshop.economy.EconomyManagerImpl;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import me.gabytm.minecraft.arcaneshop.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import me.gabytm.minecraft.arcaneshop.shop.ShopManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ArcaneShop extends JavaPlugin {

    private ArcaneShopAPI api;
    private CustomItemManager customItemManager;
    private ItemCreator itemCreator;
    private ConfigManager configManager;
    private MenuManager menuManager;

    private void registerCommands() {
        final BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);
        manager.registerCommand(new ReloadCommand(this), new ShopCommand(menuManager));
    }

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();

        this.customItemManager = new CustomItemManager();
        this.itemCreator = new ItemCreator(customItemManager);

        final EconomyManager economyManager = new EconomyManagerImpl();
        this.configManager = new ConfigManager(getDataFolder().toPath().toAbsolutePath(), itemCreator, economyManager);
        final ShopManager shopManager = new ShopManagerImpl(
                new File(getDataFolder(), "shops"),
                customItemManager
        );

        api = new ArcaneShopAPIImpl(economyManager, shopManager);
        this.menuManager = new MenuManager(api.getShopManager(), configManager, customItemManager);

        registerCommands();
        getServer().getServicesManager().register(ArcaneShopAPI.class, api, this, ServicePriority.Highest);

        // Load everything 3 seconds after startup to allow dependencies to load their stuff
        Bukkit.getScheduler().runTaskLater(this, this::load, 3 * 20L);
    }

    public void load() {
        configManager.loadMainConfig();
        configManager.loadItemsConfig();
        configManager.loadAmountSelectorMenuConfigs();

        api.getEconomyManager().setDefaultProvider(api.getEconomyManager().getProvider(configManager.getMainConfig().getDefaultEconomyProvider()));
        ((ShopManagerImpl) api.getShopManager()).loadShops(configManager);
    }

}
