package de.nike.extramodules2.potions;

import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.effects.EMMobEffects;
import net.minecraft.potion.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EMPotions {

    public static final DeferredRegister<Potion> POTIONS
            = DeferredRegister.create(ForgeRegistries.POTION_TYPES, ExtraModules2.MODID);


    public static final RegistryObject<Potion> HEALTH_BOOST =
            POTIONS.register("health_boost",
                    () -> new Potion(new EffectInstance(Effects.HEALTH_BOOST, 600, 1)));
    public static final RegistryObject<Potion> HEALTH_BOOST_STRONG =
            POTIONS.register("strong_health_boost",
                    () -> new Potion(new EffectInstance(Effects.HEALTH_BOOST, 600, 2)));

    public static final RegistryObject<Potion> WITHER =
            POTIONS.register("wither",
                    () -> new Potion(new EffectInstance(Effects.WITHER, 800, 0)));
    public static final RegistryObject<Potion> WITHER_STRONG =
            POTIONS.register("strong_wither",
                    () -> new Potion(new EffectInstance(Effects.WITHER, 400, 1)));

    public static final RegistryObject<Potion> ANTI_MATTER =
            POTIONS.register("anti_matter",
                    () -> new Potion(new EffectInstance(EMMobEffects.ANTI_MATTER.get(), 1000, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

}
