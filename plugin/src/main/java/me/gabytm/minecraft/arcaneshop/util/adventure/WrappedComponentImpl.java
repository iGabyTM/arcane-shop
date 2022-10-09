package me.gabytm.minecraft.arcaneshop.util.adventure;

import me.gabytm.minecraft.arcaneshop.api.util.adventure.WrappedComponent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class WrappedComponentImpl implements WrappedComponent {

    private final String raw;
    private final String content;
    private final Component component;

    public WrappedComponentImpl(
            @NotNull final String raw, @NotNull final String content,
            @NotNull final Component component
    ) {
        this.raw = raw;
        this.content = content;
        this.component = component;
    }

    @Override
    public @NotNull String raw() {
        return raw;
    }

    @Override
    public @NotNull String content() {
        return content;
    }

    @Override
    public @NotNull Component component() {
        return component;
    }

}
