package me.gabytm.minecraft.arcaneshop.config.serialize.shop;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopItem;
import me.gabytm.minecraft.arcaneshop.api.util.collection.Pair;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import me.gabytm.minecraft.arcaneshop.shop.ShopItemImpl;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ShopItemSerializer implements TypeSerializer<ShopItem> {

    private final ItemCreator itemCreator;

    public ShopItemSerializer(@NotNull final ItemCreator itemCreator) {
        this.itemCreator = itemCreator;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ShopItem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final Pair<DisplayItem, Boolean> displayItem = itemCreator.createFromConfig(node.node("item"));
        final int slot = node.node("slot").getInt();
        final int page = node.node("page").getInt(1);
        // If the item is invalid, set buy and sell price to 0 (invalid = pair.second() == false)
        final double buyPrice = displayItem.second() ? node.node("buyPrice").getDouble() : 0.0d;
        final double sellPrice = displayItem.second() ? node.node("sellPrice").getDouble() : 0.0d;

        return new ShopItemImpl(displayItem.first(), displayItem.first().item(), slot, page, buyPrice, sellPrice);
    }

    @Override
    public void serialize(Type type, @Nullable ShopItem obj, ConfigurationNode node) throws SerializationException {

    }

}
