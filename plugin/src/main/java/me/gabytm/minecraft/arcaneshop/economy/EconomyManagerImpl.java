package me.gabytm.minecraft.arcaneshop.economy;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.economy.providers.VaultEconomyProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EconomyManagerImpl implements EconomyManager {

    private final Map<String, EconomyProvider> economyProviders = new HashMap<>();
    private EconomyProvider defaultProvider;

    public EconomyManagerImpl() {
        register("vault", new VaultEconomyProvider(Bukkit.getServicesManager().getRegistration(Economy.class).getProvider()));
    }

    @Override
    public void register(@NotNull String name, @NotNull EconomyProvider provider) {
        this.economyProviders.put(name.toLowerCase(), provider);
    }

    @Override
    public @Nullable EconomyProvider getProvider(@NotNull String name) {
        return this.economyProviders.get(name.toLowerCase());
    }

    @Override
    public @NotNull EconomyProvider getDefaultProvider() {
        return defaultProvider;
    }

    @Override
    public void setDefaultProvider(@Nullable EconomyProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("The default economy provider can not be null");
        }

        this.defaultProvider = provider;
    }

}
