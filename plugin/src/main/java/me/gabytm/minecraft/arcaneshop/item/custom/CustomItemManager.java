package me.gabytm.minecraft.arcaneshop.item.custom;

import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemHandler;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;
import me.gabytm.minecraft.arcaneshop.item.custom.handlers.HeadDatabaseCustomItemHandler;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CustomItemManager {

    private final Map<String, CustomItemHandler<CustomItemProperties>> handlers = new HashMap<>();

    public CustomItemManager() {
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            registerHandler("HeadDatabase", new HeadDatabaseCustomItemHandler());
        }
    }

    public <P extends CustomItemProperties, C extends CustomItemHandler<P>> void registerHandler(@NotNull final String id, @NotNull final C handler) {
        //noinspection unchecked
        handlers.put(id, (CustomItemHandler<CustomItemProperties>) handler);
    }

    public CustomItemHandler<CustomItemProperties> getHandler(@NotNull final String id) {
        return handlers.get(id);
    }

}
