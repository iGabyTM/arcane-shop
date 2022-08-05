package me.gabytm.minecraft.arcaneshop.item;

import dev.triumphteam.gui.builder.item.BaseItemBuilder;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.gabytm.minecraft.arcaneshop.util.Enums;
import me.gabytm.minecraft.arcaneshop.util.ServerVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemCreator {

    private static final Material PLAYER_HEAD = ServerVersion.IS_ITEM_LEGACY ? Material.valueOf("SKULL_ITEM") : Material.PLAYER_HEAD;

    private @NotNull String getNodePath(@NotNull final CommentedConfigurationNode node) {
        return Arrays.stream(node.path().array())
                .map(Object::toString)
                .collect(Collectors.joining("."));
    }

    private boolean isPlayerHead(@NotNull final Material material, final short damage) {
        return ServerVersion.IS_ITEM_LEGACY ? (material == PLAYER_HEAD && damage == 3) : (material == PLAYER_HEAD);
    }

    private @NotNull ItemStack setMeta(@NotNull final CommentedConfigurationNode node, @NotNull final BaseItemBuilder<?> builder) throws SerializationException {
        final ItemFlag[] flags = node.node("flags").getList(String.class, Collections.emptyList()).stream()
                .map(it -> Enums.getOrNull(ItemFlag.class, it))
                .filter(Objects::nonNull)
                .toArray(ItemFlag[]::new);

        //TODO get enchantments from config
        final Map<Enchantment, Integer> enchantments = new HashMap<>();
        final int customModelData = node.node("customModelData").getInt();

        builder.name(node.node("name").get(Component.class, Component.empty()))
                .lore(node.node("lore").getList(Component.class, Collections.emptyList()))
                .flags(flags)
                .enchant(enchantments, true)
                .unbreakable(node.node("unbreakable").getBoolean());

        if (customModelData > 0) {
            builder.model(customModelData);
        }

        if (enchantments.isEmpty()) {
            builder.glow(node.node("glow").getBoolean());
        }

        return builder.build();
    }

    public ItemStack createFromConfig(@NotNull final CommentedConfigurationNode node) throws SerializationException {
        final CommentedConfigurationNode materialNode = node.node("material");

        if (materialNode.empty()) {
            return ItemBuilder.from(Material.BARRIER)
                    .name(Component.text("Missing material @ " + getNodePath(node), NamedTextColor.RED))
                    .glow()
                    .build();
        }

        final String materialName = materialNode.getString();
        final Material material = Material.matchMaterial(materialName);

        if (material == null) {
            return ItemBuilder.from(Material.BARRIER)
                    .name(Component.text(String.format("Invalid material '%s' (%s)", materialName, getNodePath(materialNode)), NamedTextColor.RED))
                    .glow()
                    .build();
        }

        final int amount = node.node("amount").getInt();
        final short damage = node.node("damage").get(Short.class, (short) 0);
        //noinspection deprecation
        final ItemBuilder builder = ItemBuilder.from(new ItemStack(material, amount, damage));
        return setMeta(node, builder);
    }

}
