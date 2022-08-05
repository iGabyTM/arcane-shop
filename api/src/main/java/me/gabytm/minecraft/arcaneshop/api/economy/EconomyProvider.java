package me.gabytm.minecraft.arcaneshop.api.economy;

import org.bukkit.entity.Player;

public interface EconomyProvider {

    boolean has(final Player player, final double amount);

    boolean subtract(final Player player, final double amount);

    boolean add(final Player player, final double amount);

    double get(final Player player);

}
