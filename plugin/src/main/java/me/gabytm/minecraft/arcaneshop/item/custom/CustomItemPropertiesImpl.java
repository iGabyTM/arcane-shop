package me.gabytm.minecraft.arcaneshop.item.custom;

import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;
import org.jetbrains.annotations.NotNull;

public class CustomItemPropertiesImpl implements CustomItemProperties {

    public static final CustomItemPropertiesImpl EMPTY = new CustomItemPropertiesImpl("");

    private final String id;

    public CustomItemPropertiesImpl(@NotNull final String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

}
