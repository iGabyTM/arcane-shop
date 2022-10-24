package me.gabytm.minecraft.arcaneshop.item.custom;

import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemHandler;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemManager;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;
import me.gabytm.minecraft.arcaneshop.item.custom.handlers.HeadDatabaseCustomItemHandler;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomItemManagerImpl implements CustomItemManager {

    private final Map<String, CustomItemHandler<CustomItemProperties>> handlers = new HashMap<>();

    public CustomItemManagerImpl() {
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            registerHandler("HeadDatabase", new HeadDatabaseCustomItemHandler());
        }
    }

    public <P extends CustomItemProperties, C extends CustomItemHandler<P>> void registerHandler(@NotNull final String id, @NotNull final C handler) {
        //noinspection unchecked
        handlers.put(id, (CustomItemHandler<CustomItemProperties>) handler);
    }

    public @Nullable CustomItemHandler<CustomItemProperties> getHandler(@NotNull final String id) {
        return handlers.get(id);
    }

}
