package de.nike.extramodules2.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class NBTHelper {

    public static void set(ItemStack stack, String tag, INBT inbt) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.put(tag, inbt);
    }

    public static INBT get(ItemStack stack, String tag, INBT fallback) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        if(compoundNBT.contains(tag)) return compoundNBT.get(tag);
        return fallback;
    }


    public static String getString(ItemStack stack, String tag, String fallback) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        if(compoundNBT.contains(tag)) return compoundNBT.getString(tag);
        return fallback;
    }

    public static void setString(ItemStack stack, String tag, String value) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.putString(tag, value);
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean b) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        if(compoundNBT.contains(tag)) return compoundNBT.getBoolean(tag);
        return b;
    }

    public static void setBoolean(ItemStack stack, String tag, boolean b) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.putBoolean(tag, b);
    }

    public static int getInt(ItemStack stack, String tag, int defaultValue) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        if(compoundNBT.contains(tag)) return compoundNBT.getInt(tag);
        return defaultValue;
    }

    public static void setInt(ItemStack stack, String tag, int value) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.putInt(tag, value);
    }

    public static float getFloat(ItemStack stack, String tag, float defaultValue) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        if(compoundNBT.contains(tag)) return compoundNBT.getFloat(tag);
        return defaultValue;
    }

    public static void setFloat(ItemStack stack, String tag, float value) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.putFloat(tag, value);
    }

}
