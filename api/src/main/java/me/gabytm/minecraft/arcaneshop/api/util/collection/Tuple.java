package me.gabytm.minecraft.arcaneshop.api.util.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tuple<A, B, C> extends Pair<A, B> {
    private final C third;

    public static @NotNull <A, B, C> Tuple<A, B, C> of(
            @Nullable final A first, @Nullable final B second,
            @Nullable final C third
    ) {
        return new Tuple<>(first, second, third);
    }

    Tuple(@Nullable final A first, @Nullable final B second, @Nullable final C third) {
        super(first, second);
        this.third = third;
    }

    public @Nullable C third() {
        return third;
    }

}
