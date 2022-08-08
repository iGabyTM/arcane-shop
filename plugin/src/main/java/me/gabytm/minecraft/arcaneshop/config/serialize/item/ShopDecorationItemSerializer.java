package me.gabytm.minecraft.arcaneshop.config.serialize.item;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.ShopDecorationItem;
import me.gabytm.minecraft.arcaneshop.config.configs.ItemsConfig;
import me.gabytm.minecraft.arcaneshop.item.ShopDecorationItemImpl;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ShopDecorationItemSerializer implements TypeSerializer<ShopDecorationItem> {

    private final ItemsConfig itemsConfig;

    public ShopDecorationItemSerializer(@NotNull final ItemsConfig itemsConfig) {
        this.itemsConfig = itemsConfig;
    }

    @Override
    public ShopDecorationItem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final DisplayItem displayItem = itemsConfig.getItems().get(node.node("id").getString());

        if (displayItem == null) {
            Logging.warning("Unknown item {0}", node.node("id").getString());
            return null;
        }

        return new ShopDecorationItemImpl(displayItem, node.node("slot").getInt(), node.node("page").getInt(-1));
    }

    @Override
    public void serialize(Type type, @Nullable ShopDecorationItem obj, ConfigurationNode node) throws SerializationException {

    }

}
