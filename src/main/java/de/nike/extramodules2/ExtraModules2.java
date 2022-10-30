package de.nike.extramodules2;

import de.nike.extramodules2.client.ClientProxy;
import de.nike.extramodules2.effects.EMMobEffects;
import de.nike.extramodules2.potions.EMPotions;
import de.nike.extramodules2.utils.NikesPotions;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.network.EMNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.Curios;

@Mod(ExtraModules2.MODID)
public class ExtraModules2 {

	public static final Logger LOGGER = LogManager.getLogger("ExtraModules");
	public static final String MODID = "extramodules2";
	public static final String MODNAME = "Extra Modules 2";

	public static CommonProxy proxy;

	public ExtraModules2() {
		proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
		proxy.construct();
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		proxy.commonSetup(event);
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		proxy.clientSetup(event);
	}

	@SubscribeEvent
	public void onServerSetup(FMLDedicatedServerSetupEvent event) {
		proxy.serverSetup(event);
	}



}
