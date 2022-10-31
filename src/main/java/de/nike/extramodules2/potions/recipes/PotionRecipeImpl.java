package de.nike.extramodules2.potions.recipes;

import de.nike.extramodules2.potions.EMPotions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class PotionRecipeImpl extends BrewingRecipe {

    public PotionRecipeImpl(Potion input, Item catalysator, Potion output) {
        super(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), input)), Ingredient.of(catalysator), PotionUtils.setPotion(new ItemStack(Items.POTION, 1), output));
    }

}
