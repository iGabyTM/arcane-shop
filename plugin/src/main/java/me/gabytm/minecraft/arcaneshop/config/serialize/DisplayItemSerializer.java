package me.gabytm.minecraft.arcaneshop.config.serialize;

import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.item.DisplayItemImpl;
import me.gabytm.minecraft.arcaneshop.item.ItemCreator;
import me.gabytm.minecraft.arcaneshop.util.ServerVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayItemSerializer implements TypeSerializer<DisplayItem> {

    private static final Field DISPLAY_NAME_FIELD;
    private static final Field LORE_FIELD;
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();
    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();

    static {
        try {
            final Class<?> metaClass = ServerVersion.craftClass("inventory.CraftMetaItem");

            DISPLAY_NAME_FIELD = metaClass.getDeclaredField("displayName");
            DISPLAY_NAME_FIELD.setAccessible(true);

            LORE_FIELD = metaClass.getDeclaredField("lore");
            LORE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException("Could not retrieve displayName nor lore field for ItemBuilder.");
        }
    }

    private final ItemCreator itemCreator;

    public DisplayItemSerializer(@NotNull final ItemCreator itemCreator) {
        this.itemCreator = itemCreator;
    }

    private @NotNull Component getName(@NotNull final ItemMeta meta) {
        if (!meta.hasDisplayName()) {
            return Component.empty();
        }

        String name;

        try {
            name = (String) DISPLAY_NAME_FIELD.get(meta);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Component.empty();
        }

        return (ServerVersion.IS_ITEM_LEGACY ? LEGACY_SERIALIZER : GSON_SERIALIZER).deserialize(name);
    }

    private @NotNull List<Component> getLore(@NotNull final ItemMeta meta) {
        if (!meta.hasLore()) {
            return Collections.emptyList();
        }

        List<String> lore;

        try {
            //noinspection unchecked
            lore = (List<String>) LORE_FIELD.get(meta);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        final ComponentSerializer<Component, ? extends Component, String> serializer = ServerVersion.IS_ITEM_LEGACY ? LEGACY_SERIALIZER : GSON_SERIALIZER;
        return lore.stream().map(serializer::deserialize).collect(Collectors.toList());
    }

    @Override
    public DisplayItem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return itemCreator.createFromConfig(node);
    }

    @Override
    public void serialize(Type type, @Nullable DisplayItem obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            return;
        }

        final ItemStack item = obj.item();
        node.node("material").set(item.getType().name());

        //noinspection deprecation
        final short durability = item.getDurability();

        if (durability != 0) {
            node.node("damage").set(durability);
        }

        if (item.hasItemMeta()) {
            final ItemMeta meta = item.getItemMeta();
            node.node("name").set(getName(meta));
            node.node("lore").setList(Component.class, getLore(meta));
        }
    }

    @Override
    public @Nullable DisplayItem emptyValue(Type specificType, ConfigurationOptions options) {
        return new DisplayItemImpl(new ItemStack(Material.AIR), "", Collections.emptyList());
    }

}
