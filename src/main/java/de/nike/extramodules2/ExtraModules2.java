package de.nike.extramodules2;

import de.nike.extramodules2.effects.EMMobEffects;
import de.nike.extramodules2.potions.EMPotions;
import de.nike.extramodules2.utils.NikesPotions;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.network.EMNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("extramodules2")
public class ExtraModules2 {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "extramodules2";

	public ExtraModules2() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		EMItems.register(eventBus);
		EMPotions.register(eventBus);
		EMMobEffects.register(eventBus);
		EMNetwork.init();

		eventBus.addListener(this::setup);
		eventBus.addListener(this::enqueueIMC);
		eventBus.addListener(this::processIMC);
		eventBus.addListener(this::doClientStuff);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Loading ExtraModules2!");
		// Potion Effects
		NikesPotions.addMix(Potions.AWKWARD, Items.WITHER_SKELETON_SKULL, EMPotions.WITHER.get());
		NikesPotions.addMix(EMPotions.WITHER.get(), Items.GLOWSTONE_DUST, EMPotions.WITHER_STRONG.get());
		NikesPotions.addMix(Potions.MUNDANE, Items.HONEYCOMB, EMPotions.HEALTH_BOOST.get());
		NikesPotions.addMix(EMPotions.HEALTH_BOOST.get(), Items.GLOWSTONE_DUST, EMPotions.HEALTH_BOOST_STRONG.get());
		
	}


	private void doClientStuff(final FMLClientSetupEvent event) {

	}

	private void enqueueIMC(final InterModEnqueueEvent event) {

	}

	private void processIMC(final InterModProcessEvent event) {

	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {

	}

	/*
	 * /@OnlyIn(Dist.CLIENT)
	 * 
	 * @SubscribeEvent public void onTextureStiching(final TextureStitchEvent event)
	 * { // Sprites.GURADIAN_EYE =
	 * event.getMap().getSprite(Sprites.guradianEyeSprite); } Note: Did not work I
	 * have no clue how to load a custom Texture as TextureAtlasSprite. /
	 */

}
