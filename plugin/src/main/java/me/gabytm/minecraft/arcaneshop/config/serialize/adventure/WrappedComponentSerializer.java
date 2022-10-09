package me.gabytm.minecraft.arcaneshop.config.serialize.adventure;

import me.gabytm.minecraft.arcaneshop.api.util.adventure.WrappedComponent;
import me.gabytm.minecraft.arcaneshop.util.adventure.WrappedComponentImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class WrappedComponentSerializer implements TypeSerializer<WrappedComponent> {

    @Override
    public WrappedComponent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final String raw = node.getString("");
        final TextComponent component = (TextComponent) node.get(Component.class, Component.empty());
        return new WrappedComponentImpl(raw, component.content(), component);
    }

    @Override
    public void serialize(Type type, @Nullable WrappedComponent obj, ConfigurationNode node) throws SerializationException {

    }

    @Override
    public @Nullable WrappedComponent emptyValue(Type specificType, ConfigurationOptions options) {
        return new WrappedComponentImpl("", "", Component.empty());
    }

}
