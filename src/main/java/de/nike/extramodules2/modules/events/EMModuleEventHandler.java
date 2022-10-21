package de.nike.extramodules2.modules.events;

import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import com.brandon3055.draconicevolution.api.modules.entities.UndyingEntity;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.ModuleTypes;
import de.nike.extramodules2.modules.entities.DefenseBrainEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EMModuleEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0 || event.getEntityLiving().level.isClientSide) {
            return;
        }

        LivingEntity entity = event.getEntityLiving();
        ItemStack chestStack = ModularChestpiece.getChestpiece(entity);
        LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);

        if (chestStack.isEmpty() || !optionalHost.isPresent()) {
            return;
        }

        ModuleHost host = optionalHost.orElseThrow(IllegalStateException::new);
        DefenseBrainEntity defenseBrain = host.getEntitiesByType(ModuleTypes.DEFENSE_BRAIN).map(e -> (DefenseBrainEntity) e).findAny().orElse(null);
        if (defenseBrain == null) {
            return;
        }
        defenseBrain.attacked(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(LivingDamageEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0 || event.getEntityLiving().level.isClientSide) {
            return;
        }

        LivingEntity entity = event.getEntityLiving();
        ItemStack chestStack = ModularChestpiece.getChestpiece(entity);
        LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);

        if (chestStack.isEmpty() || !optionalHost.isPresent()) {
            return;
        }

        ModuleHost host = optionalHost.orElseThrow(IllegalStateException::new);
        DefenseBrainEntity defenseBrain = host.getEntitiesByType(ModuleTypes.DEFENSE_BRAIN).map(e -> (DefenseBrainEntity) e).findAny().orElse(null);
        if (defenseBrain == null) {
            return;
        }
        defenseBrain.damaged(event);
    }

}
