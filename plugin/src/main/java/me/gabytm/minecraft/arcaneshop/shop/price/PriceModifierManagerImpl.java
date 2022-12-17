package me.gabytm.minecraft.arcaneshop.shop.price;

import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.price.PriceModifier;
import me.gabytm.minecraft.arcaneshop.api.shop.price.PriceModifierManager;
import me.gabytm.minecraft.arcaneshop.config.configs.PriceModifiersConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PriceModifierManagerImpl implements PriceModifierManager {

    private final Map<String, PriceModifier> loadedModifiers = new HashMap<>();

    @Override
    public @NotNull List<@NotNull PriceModifier> getPlayerModifiers(@NotNull Player player, @NotNull Shop shop) {
        return loadedModifiers.values()
                .stream()
                .filter(it -> it.getShops().isEmpty() || it.getShops().contains(shop.getName()))
                .collect(Collectors.toList());
    }

    public void loadModifiers(PriceModifiersConfig config) {
        loadedModifiers.clear();
        loadedModifiers.putAll(config.getModifiers());
    }

}
