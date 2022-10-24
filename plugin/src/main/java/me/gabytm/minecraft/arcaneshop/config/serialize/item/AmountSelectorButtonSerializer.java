package me.gabytm.minecraft.arcaneshop.config.serialize.item;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.menu.menus.amountselector.AmountSelectionAction;
import me.gabytm.minecraft.arcaneshop.menu.menus.amountselector.AmountSelectorButton;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class AmountSelectorButtonSerializer implements TypeSerializer<AmountSelectorButton> {

    @Override
    public AmountSelectorButton deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final DisplayItem displayItem = node.node("item").get(DisplayItem.class);
        final AmountSelectionAction amountAction = node.node("action").get(AmountSelectionAction.class);
        final int value = node.node("value").getInt(AmountSelectorButton.MAX);
        final int slot = node.node("slot").getInt();

        if (displayItem == null || amountAction == null) {
            Logging.warning("Could not load button {0}", node.key());
            return null;
        }

        return new AmountSelectorButton(displayItem, amountAction, value, slot);
    }

    @Override
    public void serialize(Type type, @Nullable AmountSelectorButton obj, ConfigurationNode node) throws SerializationException {

    }

}
