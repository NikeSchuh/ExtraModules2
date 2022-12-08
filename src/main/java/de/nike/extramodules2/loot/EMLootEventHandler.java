package de.nike.extramodules2.loot;


import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.items.EMItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EMLootEventHandler {

    /*/@SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityDrop(LivingDropsEvent event) {
        if(event.getEntity().level.isClientSide) return;
        if(event.getEntityLiving().getType() == EntityType.ELDER_GUARDIAN) {
            Random random = event.getEntityLiving().level.random;
            if(random.nextFloat() < Math.min(0.6f, (0.1f) + (0.05 * event.getLootingLevel()))) {
                Vector3d pos = event.getEntityLiving().position();
                event.getDrops().add(new ItemEntity(event.getEntityLiving().level, pos.x, pos.y, pos.z, new ItemStack(EMItems.ELDER_GUARDIAN_BRAIN.get(), 1 + random.nextInt((event.getLootingLevel() + 1) / 2))));
            } else if(event.getEntityLiving().level.random.nextFloat() < 0.3 + (0.1 * event.getLootingLevel())) {
                int amount = event.getEntityLiving().level.random.nextInt(2 + event.getLootingLevel()) + 1;
                Vector3d pos = event.getEntityLiving().position();
                event.getDrops().add(new ItemEntity(event.getEntityLiving().level, pos.x, pos.y, pos.z, new ItemStack(EMItems.ELDER_GUARDIAN_PARTS.get(), amount)));
            }
        }
    }/*/



}
