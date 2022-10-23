package me.gabytm.minecraft.arcaneshop.economy;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import me.gabytm.minecraft.arcaneshop.economy.providers.ExperienceEconomyProvider;
import me.gabytm.minecraft.arcaneshop.economy.providers.VaultEconomyProvider;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EconomyManagerImpl implements EconomyManager {

    private final Map<String, EconomyProvider> economyProviders = new HashMap<>();
    private EconomyProvider defaultProvider;

    public EconomyManagerImpl() {
        final EconomyProvider experience = new ExperienceEconomyProvider();
        register("experience", experience);

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            final RegisteredServiceProvider<Economy> economy = Bukkit.getServicesManager().getRegistration(Economy.class);

            if (economy == null) {
                setDefaultProvider(experience);
                Logging.warning("Could not hook into vault, using 'experience' as default economy");
            } else {
                final EconomyProvider vault = new VaultEconomyProvider(economy.getProvider());
                register("vault", vault);
                setDefaultProvider(vault);
                Logging.info("Hooked into vault, using it as default economy");
            }
        }
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
