package me.gabytm.minecraft.arcaneshop.item;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import dev.triumphteam.gui.builder.item.BaseItemBuilder;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemHandler;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;
import me.gabytm.minecraft.arcaneshop.api.item.head.HeadTextureProvider;
import me.gabytm.minecraft.arcaneshop.api.util.collection.Pair;
import me.gabytm.minecraft.arcaneshop.item.head.HeadTextureManager;
import me.gabytm.minecraft.arcaneshop.util.Enums;
import me.gabytm.minecraft.arcaneshop.util.Items;
import me.gabytm.minecraft.arcaneshop.util.Logging;
import me.gabytm.minecraft.arcaneshop.util.Separator;
import me.gabytm.minecraft.arcaneshop.util.ServerVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemCreator {

    private final HeadTextureManager headTextureManager = new HeadTextureManager();
    private final CustomItemManager customItemManager;

    public static @NotNull Component removeItalic(@NotNull final Component component) {
        return Component.empty().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE).append(component);
    }

    public ItemCreator(@NotNull final CustomItemManager customItemManager) {
        this.customItemManager = customItemManager;
    }

    private @NotNull String getNodePath(@NotNull final ConfigurationNode node) {
        return Arrays.stream(node.path().array())
                .map(Object::toString)
                .collect(Collectors.joining("."));
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    private @Nullable Map.Entry<@NotNull Enchantment, @NotNull Integer> parseEnchantmentAndLevel(@NotNull final String[] parts) {
        if (parts.length > 2) {
            return null;
        }

        Enchantment enchantment;

        if (ServerVersion.HAS_KEYS) {
            final String[] key = parts[0].split(Separator.COLON, 2);

            if (key.length == 2) {
                enchantment = Enchantment.getByKey(new NamespacedKey(key[0], key[1]));
            } else {
                // The server has NamespacedKeys but this isn't one
                enchantment = Enchantment.getByName(parts[0]);
            }
        } else {
            enchantment = Enchantment.getByName(parts[0]);
        }

        if (enchantment == null) {
            Logging.warning("{0} is not a valid enchantment", parts[0]);
            return null;
        }

        final Integer level = Ints.tryParse(parts[1]);

        if (level == null) {
            Logging.warning("{0} is not a valid level ({1};{0})", parts[1], parts[0]);
        }

        return (level == null) ? null : Maps.immutableEntry(enchantment, level);
    }

    private @NotNull DisplayItem setMeta(@NotNull final ConfigurationNode node, @NotNull final BaseItemBuilder<?> builder) throws SerializationException {
        final ItemFlag[] flags = node.node("flags")
                .getList(String.class, Collections.emptyList())
                .stream()
                .map(string -> Enums.getOrNull(ItemFlag.class, string))
                .filter(Objects::nonNull)
                .toArray(ItemFlag[]::new);

        final Map<@NotNull Enchantment, @NotNull Integer> enchantments =
                node.node("enchantments")
                        .getList(String.class, Collections.emptyList())
                        .stream()
                        .map(string -> string.split(Separator.SEMICOLON)) // Enchantments are defined as Enchantment;Level
                        .map(this::parseEnchantmentAndLevel)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final int customModelData = node.node("customModelData").getInt();
        final Component nameComponent = node.node("name").get(Component.class, Component.empty());

        if (!((TextComponent) nameComponent).content().isEmpty()) {
            builder.name(removeItalic(nameComponent));
        }

        final List<Component> loreComponent = node.node("lore")
                .getList(Component.class, Collections.emptyList())
                .stream()
                .map(ItemCreator::removeItalic)
                .collect(Collectors.toList());

        if (!loreComponent.isEmpty()) {
            builder.lore(loreComponent);
        }

        builder.flags(flags)
                .enchant(enchantments, true)
                .unbreakable(node.node("unbreakable").getBoolean());

        if (customModelData > 0) {
            builder.model(customModelData);
        }

        if (enchantments.isEmpty()) {
            builder.glow(node.node("glow").getBoolean());
        }

        final String name = node.node("name").getString("");
        final List<@NotNull String> lore = node.node("lore").getList(String.class, Collections.emptyList());
        return new DisplayItemImpl(builder.build(), name, lore);
    }

    public @NotNull Pair<@NotNull DisplayItem, @NotNull Boolean> createFromConfig(@NotNull final ConfigurationNode node) throws SerializationException {
        final ConfigurationNode materialNode = node.node("material");

        if (materialNode.empty()) {
            final ItemStack item = ItemBuilder.from(Material.BARRIER)
                    .name(removeItalic(Component.text("Missing material (" + getNodePath(node) + ')', NamedTextColor.RED)))
                    .glow()
                    .build();
            return Pair.of(new DisplayItemImpl(item, "", Collections.emptyList()), false);
        }

        final String materialName = materialNode.getString("");
        final Material material = Material.matchMaterial(materialName);

        if (material == null) {
            final ItemStack item = ItemBuilder.from(Material.BARRIER)
                    .name(removeItalic(Component.text(String.format("Invalid material '%s' (%s)", materialName, getNodePath(materialNode)), NamedTextColor.RED)))
                    .glow()
                    .build();
            return Pair.of(new DisplayItemImpl(item, "", Collections.emptyList()), false);
        }

        final int amount = node.node("amount").getInt(1);
        final short damage = node.node("damage").get(Short.class, (short) 0);

        //noinspection deprecation
        final ItemStack itemStack = new ItemStack(material, amount, damage);
        final ConfigurationNode customNode = node.node("custom");

        // The item is custom (e.g. a head from HeadDatabase)
        if (!customNode.empty()) {
            final String customItemHandlerId = customNode.node("type").getString();

            // The type is specified
            if (customItemHandlerId != null) {
                final CustomItemHandler<?> customItemHandler = customItemManager.getHandler(customItemHandlerId);

                // A handler with the provided name is registered
                if (customItemHandler != null) {
                    // Create the custom item
                    final Pair<ItemStack, ? extends CustomItemProperties> pair = customItemHandler.createItem(itemStack, customNode);
                    // Apply the general meta (e.g. name, lore)
                    final DisplayItem displayItem = setMeta(node, ItemBuilder.from(pair.first()));
                    // Return the custom item
                    return Pair.of(new DisplayItemImpl(displayItem.getItemStack(), displayItem.name(), displayItem.lore(), true, customItemHandlerId, pair.second()), true);
                }
            }
        }

        BaseItemBuilder<?> itemBuilder = ItemBuilder.from(itemStack);

        if (Items.isPlayerHead(material, damage)) {
            // prefix;value
            final String[] texture = node.node("texture").getString("").split(Separator.SEMICOLON, 2);

            // The texture is not a base64 string
            if (texture.length == 2) {
                final HeadTextureProvider provider = headTextureManager.getProvider(texture[0]);

                // A provider with the given id was found
                if (provider != null) {
                    itemBuilder = ItemBuilder.skull(provider.applyTexture(texture[1]));
                }
            } else {
                itemBuilder = ItemBuilder.skull(headTextureManager.getBase64().applyTexture(texture[0]));
            }
        }

        return Pair.of(setMeta(node, itemBuilder), true);
    }

}
