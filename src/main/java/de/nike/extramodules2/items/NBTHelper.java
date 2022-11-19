package de.nike.extramodules2.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTHelper {

    public static boolean getBoolean(ItemStack stack, String tag, boolean b) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        if(compoundNBT.contains(tag)) return compoundNBT.getBoolean(tag);
        return b;
    }

    public static void setBoolean(ItemStack stack, String tag, boolean b) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.putBoolean(tag, b);
    }


}
