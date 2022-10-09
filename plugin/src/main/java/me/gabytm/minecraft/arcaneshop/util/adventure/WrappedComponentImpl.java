package me.gabytm.minecraft.arcaneshop.util.adventure;

import me.gabytm.minecraft.arcaneshop.api.util.adventure.WrappedComponent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class WrappedComponentImpl implements WrappedComponent {

    private final String text;
    private final Component component;

    public WrappedComponentImpl(@NotNull final String text, @NotNull final Component component) {
        this.text = text;
        this.component = component;
    }

    @Override
    public @NotNull String text() {
        return text;
    }

    @Override
    public @NotNull Component component() {
        return component;
    }

}
