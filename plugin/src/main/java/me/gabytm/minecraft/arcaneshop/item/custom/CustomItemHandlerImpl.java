package me.gabytm.minecraft.arcaneshop.item.custom;

import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemHandler;
import me.gabytm.minecraft.arcaneshop.api.item.custom.CustomItemProperties;

public abstract class CustomItemHandlerImpl<P extends CustomItemProperties> implements CustomItemHandler<P> {

    @Override
    public boolean canTakeItems() {
        return true;
    }

}
