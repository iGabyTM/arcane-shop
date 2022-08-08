package me.gabytm.minecraft.arcaneshop.config.configs;

import com.google.common.collect.ImmutableMap;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class MainConfig {

    @Comment("The economy type used for all shops by default")
    @Setting("economy")
    private String defaultEconomyProvider = "vault";

    @Comment("The shop actions used for all shops by default")
    @Setting("shopActions")
    private Map<@NotNull ShopAction, @NotNull ClickType> shopActions = ImmutableMap.of(
            ShopAction.BUY, ClickType.RIGHT,
            ShopAction.BUY_MULTIPLE, ClickType.SHIFT_RIGHT,
            ShopAction.SELL, ClickType.LEFT,
            ShopAction.SELL_ALL, ClickType.SHIFT_LEFT
    );

    public String getDefaultEconomyProvider() {
        return defaultEconomyProvider;
    }

    public @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getShopActions() {
        return shopActions;
    }

}
