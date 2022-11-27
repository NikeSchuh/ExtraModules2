package de.nike.extramodules2.client;

import de.nike.extramodules2.CommonProxy;
import de.nike.extramodules2.client.entity.DraconicBulletRenderer;
import de.nike.extramodules2.client.entity.DraconicLightningChainRenderer;
import de.nike.extramodules2.effects.EMMobEffects;
import de.nike.extramodules2.entity.EMEntities;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.potions.EMPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void construct() {
        super.construct();
    }

    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EMEntities.DRACONIC_BULLET, DraconicBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EMEntities.DRACONIC_CHAIN, DraconicLightningChainRenderer::new);
        super.clientSetup(event);
    }
}
