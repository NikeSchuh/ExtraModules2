package de.nike.extramodules2.client;

import de.nike.extramodules2.CommonProxy;
import de.nike.extramodules2.effects.EMMobEffects;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.potions.EMPotions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void construct() {
        super.construct();
    }

    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        super.clientSetup(event);
    }
}
