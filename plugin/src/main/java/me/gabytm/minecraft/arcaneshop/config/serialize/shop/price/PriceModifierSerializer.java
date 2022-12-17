package me.gabytm.minecraft.arcaneshop.config.serialize.shop.price;

import me.gabytm.minecraft.arcaneshop.api.shop.price.PriceModifier;
import me.gabytm.minecraft.arcaneshop.shop.price.PriceModifierImpl;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;

public class PriceModifierSerializer implements TypeSerializer<PriceModifier> {

    public static PriceModifierSerializer INSTANCE = new PriceModifierSerializer();

    @Override
    public PriceModifier deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return new PriceModifierImpl(
                node.key().toString(),
                node.node("action").get(PriceModifier.Action.class, PriceModifier.Action.BOTH),
                new HashSet<>(node.node("shops").getList(String.class, Collections.emptyList())),
                new HashSet<>(node.node("items").getList(String.class, Collections.emptyList())),
                node.node("value").getDouble()
        );
    }

    @Override
    public void serialize(Type type, @Nullable PriceModifier obj, ConfigurationNode node) throws SerializationException {

    }

}
