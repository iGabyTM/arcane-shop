package me.gabytm.minecraft.arcaneshop.config.serialize;

import me.gabytm.minecraft.arcaneshop.api.economy.EconomyManager;
import me.gabytm.minecraft.arcaneshop.api.economy.EconomyProvider;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EconomyProviderSerializer implements TypeSerializer<EconomyProvider> {

    private final EconomyManager economyManager;

    public EconomyProviderSerializer(@NotNull final EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public EconomyProvider deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final String provider = node.getString();
        return (provider == null) ? null : economyManager.getProvider(provider);
    }

    @Override
    public void serialize(Type type, @Nullable EconomyProvider obj, ConfigurationNode node) throws SerializationException {

    }

}
