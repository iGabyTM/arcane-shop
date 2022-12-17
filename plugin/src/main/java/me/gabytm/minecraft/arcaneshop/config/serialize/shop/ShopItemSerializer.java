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
import java.util.Collections;
import java.util.List;

public class ShopItemSerializer implements TypeSerializer<ShopItem> {

    private final ItemCreator itemCreator;

    public ShopItemSerializer(@NotNull final ItemCreator itemCreator) {
        this.itemCreator = itemCreator;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ShopItem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final Pair<DisplayItem, Boolean> item = itemCreator.createFromConfig(node.node("item"));
        final Pair<DisplayItem, Boolean> displayItem = itemCreator.createFromConfig(node.node("displayItem"));

        if (!item.second() && !displayItem.second()) {
            return new ShopItemImpl(node.key().toString(), item.first());
        }

        final List<String> commands = node.node("commands", "list").getList(String.class, Collections.emptyList());
        final boolean executeCommandsOnceForAllItems = node.node("commands", "executeOnceForAllItems").getBoolean();

        final int amount = node.node("amount").getInt(1);
        final int slot = node.node("slot").getInt();
        final int page = node.node("page").getInt(1);
        final double buyPrice = node.node("buyPrice").getDouble();
        final double sellPrice = node.node("sellPrice").getDouble();
        final boolean acceptOnlyExactItems = node.node("acceptOnlyExactItems").getBoolean(true);

        return new ShopItemImpl(
                node.key().toString(),
                displayItem.second() ? displayItem.first() : item.first(),
                item.first(),
                commands,
                executeCommandsOnceForAllItems,
                amount,
                slot,
                page,
                (buyPrice > 0.0 ? buyPrice / amount : 0.0),
                // Commands can not be sold
                (sellPrice > 0.0 && commands.isEmpty() ? sellPrice / amount : 0.0),
                acceptOnlyExactItems
        );
    }

    @Override
    public void serialize(Type type, @Nullable ShopItem obj, ConfigurationNode node) throws SerializationException {

    }

}
