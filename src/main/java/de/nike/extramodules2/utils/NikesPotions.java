package de.nike.extramodules2.utils;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;

import java.lang.reflect.Method;

public class NikesPotions {

    public static void addMix(Potion catalyst, Item item, Potion result) {
        try {
           Method method =  PotionBrewing.class.getDeclaredMethod("addMix", Potion.class, Item.class, Potion.class);
           method.setAccessible(true);
           method.invoke(null, catalyst, item, result);
        }catch (Exception e) {
            ExtraModules2.LOGGER.error("Failed to register custom brewing recipe ;(");
            e.printStackTrace();
        }
    }

}
