package de.nike.extramodules2;

import com.sun.prism.TextureMap;
import de.nike.extramodules2.modules.sprites.Sprites;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("extramodules2")
public class ExtraModules2
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "extramodules2";

    public ExtraModules2() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Loading ExtraModules2!");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {

    }

    private void processIMC(final InterModProcessEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }


    /*/@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onTextureStiching(final TextureStitchEvent event) {
     //   Sprites.GURADIAN_EYE = event.getMap().getSprite(Sprites.guradianEyeSprite);
    }
    Note: Did not work I have no clue how to load a custom Texture as TextureAtlasSprite.
    /*/

}
