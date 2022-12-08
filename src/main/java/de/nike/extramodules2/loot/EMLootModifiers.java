package de.nike.extramodules2.loot;

import com.brandon3055.draconicevolution.datagen.LootTableGenerator;
import de.nike.extramodules2.ExtraModules2;
import net.minecraft.loot.LootTableManager;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EMLootModifiers {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, ExtraModules2.MODID);

    public static final RegistryObject<GuardianDropAdderModifier.Serializer> GUARDIAN_DROPS = MODIFIERS.register("guardian_drop", GuardianDropAdderModifier.Serializer::new);


    @SubscribeEvent
    public static void runData(GatherDataEvent event) {
        event.getGenerator().addProvider(new DataProvider(event.getGenerator(), ExtraModules2.MODID));
    }

}
