package me.gabytm.minecraft.arcaneshop.commands;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import me.gabytm.minecraft.arcaneshop.ArcaneShop;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command("shop")
public class ReloadCommand extends BaseCommand {

    private final ArcaneShop plugin;

    public ReloadCommand(@NotNull final ArcaneShop plugin) {
        this.plugin = plugin;
    }

    @SubCommand("reload")
    public void onCommand(@NotNull final CommandSender sender) {
        plugin.load();
        sender.sendMessage("Reloading...");
    }

}
