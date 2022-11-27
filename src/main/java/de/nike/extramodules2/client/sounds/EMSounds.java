package de.nike.extramodules2.client.sounds;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@ObjectHolder(ExtraModules2.MODID)
@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = MOD)
public class EMSounds {

    @ObjectHolder("thunder_strike")
    public static SoundEvent thunderStrike;

    @ObjectHolder("modular_pistol_shoot")
    public static SoundEvent modularPistolShoot;

    @ObjectHolder("distant_thunder")
    public static SoundEvent distantThunder;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(new SoundEvent(new ResourceLocation(ExtraModules2.MODID, "thunder_strike")).setRegistryName("thunder_strike"));
        event.getRegistry().register(new SoundEvent(new ResourceLocation(ExtraModules2.MODID, "modular_pistol_shoot")).setRegistryName("modular_pistol_shoot"));
        event.getRegistry().register(new SoundEvent(new ResourceLocation(ExtraModules2.MODID, "distant_thunder")).setRegistryName("distant_thunder"));

    }

}
