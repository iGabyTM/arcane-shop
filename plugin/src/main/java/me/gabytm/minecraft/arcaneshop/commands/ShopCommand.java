package me.gabytm.minecraft.arcaneshop.commands;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import me.gabytm.minecraft.arcaneshop.menu.MenuManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("shop")
public class ShopCommand extends BaseCommand {

    private final MenuManager menuManager;

    public ShopCommand(@NotNull final MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @Default
    public void onCommand(@NotNull final Player sender) {
        menuManager.openMain(sender);
    }

}
