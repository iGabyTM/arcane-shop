package me.gabytm.minecraft.arcaneshop.config.configs;

import me.gabytm.minecraft.arcaneshop.menu.menus.amountselector.AmountSelectorButton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class AmountSelectorMenuConfig {

    private Component title = Component.empty();
    private int rows = 6;
    @Setting("itemSlot")
    private int itemSlot = 10;

    private Map<@NotNull String, @Nullable AmountSelectorButton> buttons = new HashMap<>();


    public Component getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public int getItemSlot() {
        return itemSlot;
    }

    public Map<String, AmountSelectorButton> getButtons() {
        return buttons;
    }

}
