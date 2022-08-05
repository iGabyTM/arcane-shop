package me.gabytm.minecraft.arcaneshop;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import me.gabytm.minecraft.arcaneshop.api.ArcaneShopAPI;
import me.gabytm.minecraft.arcaneshop.api.ArcaneShopAPIImpl;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.commands.ShopCommand;
import me.gabytm.minecraft.arcaneshop.config.ConfigManager;
import me.gabytm.minecraft.arcaneshop.economy.EconomyManagerImpl;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import me.gabytm.minecraft.arcaneshop.shop.ShopManagerImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class ArcaneShop extends JavaPlugin {

    private ArcaneShopAPI api;
    private ConfigManager configManager;
    private MenuManager menuManager;

    private void registerCommands() {
        final BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);
        manager.registerCommand(new ShopCommand(menuManager));
    }

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this.getDataFolder());
        final EconomyManager economyManager = new EconomyManagerImpl();

        api = new ArcaneShopAPIImpl(economyManager, new ShopManagerImpl(economyManager));
        this.menuManager = new MenuManager(api.getShopManager());
        registerCommands();

        getServer().getServicesManager().register(ArcaneShopAPI.class, api, this, ServicePriority.Highest);
        System.out.println("configManager.getConfig().getEconomy() = " + configManager.getConfig().getEconomy());
        System.out.println("configManager.getConfig().getShopActions() = " + configManager.getConfig().getShopActions());
    }

}
