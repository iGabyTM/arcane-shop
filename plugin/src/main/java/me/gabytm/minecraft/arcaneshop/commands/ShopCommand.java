package me.gabytm.minecraft.arcaneshop.commands;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import me.gabytm.minecraft.arcaneshop.menu.MainMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("shop")
public class ShopCommand extends BaseCommand {

    private final MainMenu mainMenu;

    public ShopCommand(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Default
    public void onCommand(@NotNull final Player sender) {
        mainMenu.open(sender);
    }

}
