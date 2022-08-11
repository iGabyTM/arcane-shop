package me.gabytm.minecraft.arcaneshop.config.configs;

import me.gabytm.minecraft.arcaneshop.menu.menus.amountselector.AmountSelectorButton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class AmountSelectorMenuConfig {

    private Component title = Component.empty();
    private int rows = 6;

    private Item item;

    private Map<@NotNull String, @Nullable AmountSelectorButton> buttons = new HashMap<>();


    public Component getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public Item item() {
        return item;
    }

    public Map<String, AmountSelectorButton> getButtons() {
        return buttons;
    }

    @ConfigSerializable
    public static class Item {

        private int slot = 4;
        private List<String> lore = Collections.emptyList();

        public int getSlot() {
            return slot;
        }

        public List<String> getLore() {
            return lore;
        }

    }

}
