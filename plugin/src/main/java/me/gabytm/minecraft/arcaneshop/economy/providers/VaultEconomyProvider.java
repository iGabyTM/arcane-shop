package me.gabytm.minecraft.arcaneshop.economy.providers;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VaultEconomyProvider implements EconomyProvider {

    private final Economy economy;

    public VaultEconomyProvider(@NotNull final Economy economy) {
        this.economy = economy;
    }

    @Override
    public boolean hasEnough(Player player, double amount) {
        return economy.has(player, amount);
    }

    @Override
    public boolean subtract(Player player, double amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean add(Player player, double amount) {
        return economy.depositPlayer(player, amount).transactionSuccess();
    }

}
