package me.gabytm.minecraft.arcaneshop.util;

public final class Separator {

    // Used for NamespaceKeys to separate the namespace from the key
    public static final String COLON = ":";
    // Used for options such as item.texture to separate the type from the value
    public static final String SEMICOLON = ";";

    private Separator() {
        throw new AssertionError("This class can not be instantiated");
    }

}
