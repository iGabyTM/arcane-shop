package me.gabytm.minecraft.arcaneshop.shop;

import com.google.common.collect.ImmutableMap;
import me.gabytm.minecraft.arcaneshop.api.shop.Shop;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShopManagerImpl implements ShopManager {

    private final Map<String, Shop> shops = new HashMap<>();

    public ShopManagerImpl() {
        final Shop blocks = new ShopImpl(
                new ItemStack(Material.BRICKS),
                Component.text("Blocks"),
                0,
                Arrays.asList(
                        new ShopItemImpl(
                                new ItemStack(Material.STONE),
                                0,
                                1,
                                1.0,
                                0.5
                        ),
                        new ShopItemImpl(
                                new ItemStack(Material.DIRT),
                                1,
                                1,
                                1.0,
                                0.5
                        )
                ),
                null
        );

        final Shop ores = new ShopImpl(
                new ItemStack(Material.DIAMOND),
                Component.text("Ores"),
                1,
                Arrays.asList(
                        new ShopItemImpl(
                                new ItemStack(Material.DIAMOND),
                                0,
                                1,
                                100,
                                25
                        ),
                        new ShopItemImpl(
                                new ItemStack(Material.EMERALD),
                                1,
                                1,
                                100,
                                25
                        )
                ),
                null
        );

        shops.put("blocks", blocks);
        shops.put("ores", ores);
    }

    @Override
    public @Nullable Shop getShop(@NotNull final String name) {
        return shops.get(name.toLowerCase());
    }

    @Override
    public @NotNull Map<String, Shop> getShops() {
        return ImmutableMap.copyOf(shops);
    }

}
