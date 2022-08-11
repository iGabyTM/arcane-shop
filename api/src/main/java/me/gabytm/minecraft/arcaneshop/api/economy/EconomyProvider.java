package me.gabytm.minecraft.arcaneshop.api.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface EconomyProvider {

    boolean has(@NotNull final Player player, final double amount);

    boolean subtract(@NotNull final Player player, final double amount);

    boolean add(@NotNull final Player player, final double amount);

    double get(@NotNull final Player player);

}
