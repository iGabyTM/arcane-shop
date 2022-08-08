package me.gabytm.minecraft.arcaneshop.util;

import me.gabytm.minecraft.arcaneshop.ArcaneShop;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.logging.Logger;

public final class Logging {

    private static final Logger logger = JavaPlugin.getPlugin(ArcaneShop.class).getLogger();

    private Logging() {
        throw new AssertionError("This class can not be instantiated");
    }

    public static void warning(@NotNull final String message, @Nullable Object... args) {
        logger.warning(MessageFormat.format(message, args));
    }

}
