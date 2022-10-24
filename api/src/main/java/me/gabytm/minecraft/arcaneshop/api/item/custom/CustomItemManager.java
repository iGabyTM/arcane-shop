package me.gabytm.minecraft.arcaneshop.api.item.custom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomItemManager {

    <P extends CustomItemProperties, C extends CustomItemHandler<P>> void registerHandler(@NotNull final String id, @NotNull final C handler);

    @Nullable CustomItemHandler<CustomItemProperties> getHandler(@NotNull final String id);

}
