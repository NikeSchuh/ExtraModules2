package de.nike.extramodules2.effects;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EMMobEffects {

    public static DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, ExtraModules2.MODID);

    public static final RegistryObject<Effect> ANTI_MATTER = EFFECTS.register("anti_matter", () -> new AntiMatterEffect());

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

}
