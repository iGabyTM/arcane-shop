package me.gabytm.minecraft.arcaneshop.api.economy;

import org.bukkit.entity.Player;

public interface EconomyProvider {

    boolean hasEnough(final Player player, final double amount);

    boolean subtract(final Player player, final double amount);

    boolean add(final Player player, final double amount);

}
