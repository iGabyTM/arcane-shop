package me.gabytm.minecraft.arcaneshop.api.util.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Pair<A, B> {

    private final A first;
    private final B second;

    public static @NotNull <A, B> Pair<A, B> of(@Nullable final A first, @Nullable final B second) {
        return new Pair<>(first, second);
    }

    Pair(@Nullable final A first, @Nullable final B second) {
        this.first = first;
        this.second = second;
    }

    public @Nullable A first() {
        return first;
    }

    public @Nullable B second() {
        return second;
    }

}
