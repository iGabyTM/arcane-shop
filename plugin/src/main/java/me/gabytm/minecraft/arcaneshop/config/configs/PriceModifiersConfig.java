package me.gabytm.minecraft.arcaneshop.config.configs;

import me.gabytm.minecraft.arcaneshop.api.shop.price.PriceModifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
public class PriceModifiersConfig {

    private final Map<String, PriceModifier> modifiers = new HashMap<>();

    public Map<String, PriceModifier> getModifiers() {
        return modifiers;
    }

}
