package me.gabytm.minecraft.arcaneshop.config.configs;

import com.google.common.collect.ImmutableMap;
import me.gabytm.minecraft.arcaneshop.api.item.DisplayItem;
import me.gabytm.minecraft.arcaneshop.item.DisplayItemImpl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Collections;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class ItemsConfig {

    private Map<@NotNull String, @NotNull DisplayItem> items = ImmutableMap.of(
            "balance", new DisplayItemImpl(new ItemStack(Material.PAPER), "", Collections.emptyList())
    );

    public @NotNull Map<@NotNull String, @NotNull DisplayItem> getItems() {
        return items;
    }

}
