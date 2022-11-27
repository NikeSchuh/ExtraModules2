package de.nike.extramodules2.potions;

import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.effects.EMMobEffects;
import de.nike.extramodules2.potions.recipes.PotionRecipeImpl;
import de.nike.extramodules2.utils.NikesMath;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EMPotions {

    public static final DeferredRegister<Potion> POTIONS
            = DeferredRegister.create(ForgeRegistries.POTION_TYPES, ExtraModules2.MODID);


    public static final RegistryObject<Potion> HEALTH_BOOST =
            POTIONS.register("health_boost",
                    () -> new Potion(new EffectInstance(Effects.HEALTH_BOOST, NikesMath.minutesToTicks(8f), 1)));
    public static final RegistryObject<Potion> HEALTH_BOOST_STRONG =
            POTIONS.register("strong_health_boost",
                    () -> new Potion(new EffectInstance(Effects.HEALTH_BOOST, NikesMath.minutesToTicks(8f), 4)));

    public static final RegistryObject<Potion> WITHER =
            POTIONS.register("wither",
                    () -> new Potion(new EffectInstance(Effects.WITHER, NikesMath.minutesToTicks(0.5f), 0)));
    public static final RegistryObject<Potion> WITHER_STRONG =
            POTIONS.register("strong_wither",
                    () -> new Potion(new EffectInstance(Effects.WITHER, NikesMath.minutesToTicks(0.5f), 1)));

    public static final RegistryObject<Potion> ANTI_MATTER =
            POTIONS.register("anti_matter",
                    () -> new Potion(new EffectInstance(EMMobEffects.ANTI_MATTER.get(), 1000, 0)));

    public static final RegistryObject<Potion> BAD_OMEN =
            POTIONS.register("bad_omen",
                    () -> new Potion(new EffectInstance(Effects.BAD_OMEN, NikesMath.minutesToTicks(10f), 0)));

    public static final RegistryObject<Potion> HASTE =
            POTIONS.register("haste",
                    () -> new Potion(new EffectInstance(Effects.DIG_SPEED, NikesMath.minutesToTicks(2f), 0)));

    public static final RegistryObject<Potion> STRONG_HASTE =
            POTIONS.register("strong_haste",
                    () -> new Potion(new EffectInstance(Effects.DIG_SPEED, NikesMath.minutesToTicks(2f), 1)));


    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
    public static void registerRecipes() {
        BrewingRecipeRegistry.addRecipe(new PotionRecipeImpl(Potions.STRONG_POISON, Items.WITHER_ROSE, EMPotions.WITHER.get()));
        BrewingRecipeRegistry.addRecipe(new PotionRecipeImpl(EMPotions.WITHER.get(), Items.GLOWSTONE_DUST, EMPotions.WITHER_STRONG.get()));
        BrewingRecipeRegistry.addRecipe(new PotionRecipeImpl(Potions.MUNDANE, Items.HONEYCOMB, EMPotions.HEALTH_BOOST.get()));
        BrewingRecipeRegistry.addRecipe(new PotionRecipeImpl(EMPotions.HEALTH_BOOST.get(), Items.GLOWSTONE_DUST, EMPotions.HEALTH_BOOST_STRONG.get()));
        BrewingRecipeRegistry.addRecipe(new PotionRecipeImpl(Potions.THICK, Items.TOTEM_OF_UNDYING, EMPotions.BAD_OMEN.get()));
        BrewingRecipeRegistry.addRecipe(new PotionRecipeImpl(Potions.STRONG_SWIFTNESS, Items.SUGAR, EMPotions.HASTE.get()));
        BrewingRecipeRegistry.addRecipe(new PotionRecipeImpl(EMPotions.HASTE.get(), Items.GLOWSTONE_DUST, EMPotions.STRONG_HASTE.get()));
    }

}
