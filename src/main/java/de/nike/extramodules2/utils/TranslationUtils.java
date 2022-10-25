package de.nike.extramodules2.utils;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

public class TranslationUtils {

    public static String getTranslation(String key) {
        return new TranslationTextComponent(key).withStyle(Style.EMPTY).getString();
    }

    public static StringTextComponent string(String s) {
        return new StringTextComponent(s);
    }
}
