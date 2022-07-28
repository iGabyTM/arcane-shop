package me.gabytm.minecraft.arcaneshop;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import me.gabytm.minecraft.arcaneshop.api.ArcaneShopAPI;
import me.gabytm.minecraft.arcaneshop.api.ArcaneShopAPIImpl;
import me.gabytm.minecraft.arcaneshop.commands.ShopCommand;
import me.gabytm.minecraft.arcaneshop.menu.MainMenu;
import me.gabytm.minecraft.arcaneshop.shop.ShopManagerImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class ArcaneShop extends JavaPlugin {

    private ArcaneShopAPI api;

    private void registerCommands() {
        final BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);
        manager.registerCommand(new ShopCommand(new MainMenu(api.getShopManager())));
    }

    @Override
    public void onEnable() {
        api = new ArcaneShopAPIImpl(new ShopManagerImpl());
        registerCommands();

        getServer().getServicesManager().register(ArcaneShopAPI.class, api, this, ServicePriority.Highest);
    }

}
