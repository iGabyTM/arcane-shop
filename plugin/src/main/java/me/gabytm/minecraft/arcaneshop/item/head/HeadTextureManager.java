package me.gabytm.minecraft.arcaneshop.item.head;

import me.gabytm.minecraft.arcaneshop.api.item.head.HeadTextureProvider;
import me.gabytm.minecraft.arcaneshop.item.head.providers.Base64HeadTextureProvider;
import me.gabytm.minecraft.arcaneshop.item.head.providers.HdbHeadTextureProvider;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class HeadTextureManager {

    private final Map<String, HeadTextureProvider> providers = new HashMap<>();
    private final HeadTextureProvider base64 = new Base64HeadTextureProvider();

    public HeadTextureManager() {
        registerProvider("base64", base64);

        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            registerProvider("hdb", new HdbHeadTextureProvider());
        }
    }

    public HeadTextureProvider getBase64() {
        return base64;
    }

    public void registerProvider(@NotNull final String prefix, @NotNull final HeadTextureProvider provider) {
        providers.put(prefix, provider);
    }

    public @Nullable HeadTextureProvider getProvider(@NotNull final String prefix) {
        return providers.get(prefix);
    }

}
