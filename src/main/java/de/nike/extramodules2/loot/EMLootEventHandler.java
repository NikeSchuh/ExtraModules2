package de.nike.extramodules2.loot;


import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.items.EMItems;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EMLootEventHandler {

   /*/ @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityDrop(LivingDropsEvent event) {
        if(event.getEntity().level.isClientSide) return;
        if(event.getEntityLiving() instanceof ElderGuardianEntity) {
            if(event.getEntityLiving().level.random.nextFloat() < 0.1f) {
                Vector3d pos = event.getEntityLiving().position();
                event.getDrops().add(new ItemEntity(event.getEntityLiving().level, pos.x, pos.y, pos.z, new ItemStack(EMItems.ELDER_GUARDIAN_BRAIN.get(), 1)));

            } else if(event.getEntityLiving().level.random.nextFloat() < 0.3f) {
                int amount = event.getEntityLiving().level.random.nextInt(2) + 1;
                Vector3d pos = event.getEntityLiving().position();
                event.getDrops().add(new ItemEntity(event.getEntityLiving().level, pos.x, pos.y, pos.z, new ItemStack(EMItems.ELDER_GUARDIAN_PARTS.get(), amount)));

            }
        }
    }/*/

}
