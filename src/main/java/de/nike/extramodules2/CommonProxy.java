package de.nike.extramodules2;

import com.brandon3055.draconicevolution.handlers.DESounds;
import de.nike.extramodules2.config.EMConfig;
import de.nike.extramodules2.effects.EMMobEffects;
import de.nike.extramodules2.entity.EMEntities;
import de.nike.extramodules2.integration.curios.EMCuriosIntegration;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.network.EMNetwork;
import de.nike.extramodules2.potions.EMPotions;
import de.nike.extramodules2.potions.recipes.PotionRecipeImpl;
import de.nike.extramodules2.utils.NikesPotions;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class CommonProxy {

    public void construct() {
        EMNetwork.init();
        EMConfig.register();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EMItems.register(eventBus);
        EMPotions.register(eventBus);
        EMMobEffects.register(eventBus);
    }


    public void clientSetup(FMLClientSetupEvent event) {

    }

    public void commonSetup(FMLCommonSetupEvent event) {
        ExtraModules2.LOGGER.info("Registering Potion mixes...");
        EMPotions.registerRecipes();
        EMCuriosIntegration.init();
    }

    public void serverSetup(FMLDedicatedServerSetupEvent event) {

    }

}
