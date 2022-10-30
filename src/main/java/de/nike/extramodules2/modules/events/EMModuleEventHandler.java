package de.nike.extramodules2.modules.events;

import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.entities.ArmorEntity;
import de.nike.extramodules2.modules.entities.ExtraHealthEntity;
import de.nike.extramodules2.modules.entities.HitCooldownEntitiy;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EMModuleEventHandler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event.isCanceled() || event.getAmount() <= 0) {
			return;
		}

		LivingEntity entity = event.getEntityLiving();
		ItemStack chestStack = ModularChestpiece.getChestpiece(entity);
		LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
		if (chestStack.isEmpty() || !optionalHost.isPresent()) {
			return;
		}
		ModuleHost host = optionalHost.orElseThrow(IllegalStateException::new);
		DefenseBrainEntity defenseBrain = host.getEntitiesByType(EMModuleTypes.DEFENSE_BRAIN).map(e -> (DefenseBrainEntity) e).findAny().orElse(null);
		if (defenseBrain == null) {
			return;
		}
		defenseBrain.attacked(event);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onLivingDamage(LivingAttackEvent event) {
		if(event.isCanceled()) return;
		if(event.getAmount() < 0) return;
		if(!(event.getEntityLiving() instanceof ServerPlayerEntity)) return;

		ServerPlayerEntity entity = (ServerPlayerEntity) event.getEntityLiving();
		ItemStack chestStack = ModularChestpiece.getChestpiece(entity);
		LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);

		if (chestStack.isEmpty() || !optionalHost.isPresent()) {
			return;
		}

		ModuleHost host = optionalHost.orElseThrow(IllegalStateException::new);
		HitCooldownEntitiy hitCooldownEntitiy = host.getEntitiesByType(EMModuleTypes.HIT_COOLDOWN).map(e -> (HitCooldownEntitiy) e).findAny().orElse(null);
		if (hitCooldownEntitiy == null) {
			return;
		}
		hitCooldownEntitiy.damaged(entity, event);
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Start event) {
		if (event.isCanceled() || event.getExplosion().getSourceMob() == null) {
			return;
		}
		Entity exploder = event.getExplosion().getSourceMob();
		if (exploder instanceof CreeperEntity) {
			CreeperEntity entity = (CreeperEntity) exploder;
			for (PlayerEntity player : entity.level.getNearbyPlayers(EntityPredicate.DEFAULT, entity, new AxisAlignedBB(entity.position().add(5, 5, 5), entity.position().subtract(5, 5, 5)))) {
				ItemStack chestStack = ModularChestpiece.getChestpiece(player);
				LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
				if (chestStack.isEmpty() || !optionalHost.isPresent()) {
					return;
				}

				ModuleHost host = optionalHost.orElseThrow(IllegalStateException::new);
				DefenseBrainEntity defenseBrain = host.getEntitiesByType(EMModuleTypes.DEFENSE_BRAIN).map(e -> (DefenseBrainEntity) e).findAny().orElse(null);
				if (defenseBrain == null) {
					return;
				}
				defenseBrain.creeperExplode((CreeperEntity) exploder, (ServerPlayerEntity) player, event);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEquip(LivingEquipmentChangeEvent event) {
		if(event.isCanceled()) return;
		if(!(event.getEntityLiving() instanceof ServerPlayerEntity)) return;
		if(event.getTo().isEmpty()) return;
		if(!(event.getTo().getItem() instanceof IModularItem)) return;
		if(!(event.getFrom().isEmpty())) return;
		if(!(event.getSlot().getType() == EquipmentSlotType.Group.ARMOR)) return;
		ServerPlayerEntity entity = (ServerPlayerEntity) event.getEntityLiving();
		ItemStack chestStack = event.getTo();
		LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
		optionalHost.ifPresent(host -> {
			ArmorEntity.equip(entity, host);
			ExtraHealthEntity.equip(entity, host);
		});
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onUnEquip(LivingEquipmentChangeEvent event) {
		if(event.isCanceled()) return;
		if(!(event.getEntityLiving() instanceof ServerPlayerEntity)) return;
		if(!(event.getFrom().getItem() instanceof IModularItem)) return;
		if(!(event.getTo().isEmpty())) return;
		if(!(event.getSlot().getType() == EquipmentSlotType.Group.ARMOR)) return;
		ServerPlayerEntity entity = (ServerPlayerEntity) event.getEntityLiving();
		ItemStack chestStack = event.getFrom();
		LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
		optionalHost.ifPresent(host -> {
			ArmorEntity.unequip(entity, host);
			ExtraHealthEntity.unequip(entity, host);
		});
	}


	@SubscribeEvent
	public static void curioEquip(CurioChangeEvent event) {
		if(event.isCanceled()) return;
		if(!(event.getEntityLiving() instanceof ServerPlayerEntity)) return;
		if(event.getTo().isEmpty()) return;
		if(!(event.getTo().getItem() instanceof IModularItem)) return;
		if(!(event.getFrom().isEmpty())) return;
		ServerPlayerEntity entity = (ServerPlayerEntity) event.getEntityLiving();
		ItemStack chestStack = event.getTo();
		LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
		optionalHost.ifPresent(host -> {
			ArmorEntity.equip(entity, host);
			ExtraHealthEntity.equip(entity, host);
		});
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void curioUnequip(CurioChangeEvent event) {
		if(event.isCanceled()) return;
		if(!(event.getEntityLiving() instanceof ServerPlayerEntity)) return;
		if(!(event.getFrom().getItem() instanceof IModularItem)) return;
		if(!(event.getTo().isEmpty())) return;
		ServerPlayerEntity entity = (ServerPlayerEntity) event.getEntityLiving();
		ItemStack chestStack = event.getFrom();
		LazyOptional<ModuleHost> optionalHost = chestStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
		optionalHost.ifPresent(host -> {
			ArmorEntity.unequip(entity, host);
			ExtraHealthEntity.unequip(entity, host);
		});
	}

}
