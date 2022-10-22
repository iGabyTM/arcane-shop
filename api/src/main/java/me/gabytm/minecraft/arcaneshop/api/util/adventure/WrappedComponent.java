package me.gabytm.minecraft.arcaneshop.api.util.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface WrappedComponent {

    /**
     * Get the value read from config, with {@link net.kyori.adventure.text.minimessage.MiniMessage} tags and everything
     *
     * @return the raw value
     */
    @NotNull String raw();

    /**
     * Get the play text content
     *
     * @return the plain text content
     * @see TextComponent#content()
     */
    @NotNull String content();

    @ApiStatus.Internal
    @NotNull Component component();

}
