package me.gabytm.minecraft.arcaneshop.config;

import com.google.common.collect.ImmutableMap;
import me.gabytm.minecraft.arcaneshop.api.shop.ShopAction;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class Config {

    @Comment("The economy type used for all shops by default")
    private String economy = "vault";
    @Comment("The shop actions used for all shops by default")
    private Map<@NotNull ShopAction, @NotNull ClickType> shopActions = ImmutableMap.of(
            ShopAction.BUY, ClickType.RIGHT,
            ShopAction.SELL, ClickType.LEFT,
            ShopAction.SELL_ALL, ClickType.SHIFT_LEFT
    );

    public String getEconomy() {
        return economy;
    }

    public @NotNull Map<@NotNull ShopAction, @NotNull ClickType> getShopActions() {
        return shopActions;
    }

}
