package me.gabytm.minecraft.arcaneshop.api.util.adventure;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface WrappedComponent {

    @NotNull String text();

    @NotNull Component component();

}
