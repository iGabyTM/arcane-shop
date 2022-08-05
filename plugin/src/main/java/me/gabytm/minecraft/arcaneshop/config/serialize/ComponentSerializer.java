package me.gabytm.minecraft.arcaneshop.config.serialize;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ComponentSerializer implements TypeSerializer<Component> {

    public static final ComponentSerializer INSTANCE = new ComponentSerializer();

    @Override
    public Component deserialize(Type type, ConfigurationNode node) {
        final String string = node.getString();
        return (string == null) ? Component.empty() : MiniMessage.miniMessage().deserialize(string);
    }

    @Override
    public void serialize(Type type, @Nullable Component obj, ConfigurationNode node) throws SerializationException {
        node.set(Component.class, (obj == null) ? "" : MiniMessage.miniMessage().serialize(obj));
    }

    @Override
    public @Nullable Component emptyValue(Type specificType, ConfigurationOptions options) {
        return Component.empty();
    }

}
