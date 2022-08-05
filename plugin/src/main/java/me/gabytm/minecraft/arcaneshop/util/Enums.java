package me.gabytm.minecraft.arcaneshop.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Enums {

    public static @Nullable <E extends Enum<E>> E getOrNull(@NotNull final Class<E> clazz, @NotNull final String constant) {
        return com.google.common.base.Enums.getIfPresent(clazz, constant.toUpperCase()).orNull();
    }

}
