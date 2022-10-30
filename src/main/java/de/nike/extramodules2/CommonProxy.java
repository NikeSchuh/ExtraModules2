package de.nike.extramodules2;

import de.nike.extramodules2.config.EMConfig;
import de.nike.extramodules2.effects.EMMobEffects;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.network.EMNetwork;
import de.nike.extramodules2.potions.EMPotions;
import de.nike.extramodules2.utils.NikesPotions;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        NikesPotions.addMix(Potions.STRONG_POISON, Items.WITHER_SKELETON_SKULL, EMPotions.WITHER.get());
        NikesPotions.addMix(EMPotions.WITHER.get(), Items.GLOWSTONE_DUST, EMPotions.WITHER_STRONG.get());
        NikesPotions.addMix(Potions.MUNDANE, Items.HONEYCOMB, EMPotions.HEALTH_BOOST.get());
        NikesPotions.addMix(EMPotions.HEALTH_BOOST.get(), Items.GLOWSTONE_DUST, EMPotions.HEALTH_BOOST_STRONG.get());
    }

    public void serverSetup(FMLDedicatedServerSetupEvent event) {

    }

}
