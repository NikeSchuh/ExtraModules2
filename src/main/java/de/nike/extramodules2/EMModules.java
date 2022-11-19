package de.nike.extramodules2;

import java.util.*;
import java.util.function.Function;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.client.utils.CyclingItemGroup;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.DamageData;
import com.brandon3055.draconicevolution.api.modules.lib.BaseModule;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleImpl;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleItem;
import com.brandon3055.draconicevolution.init.ModuleCfg;

import de.nike.extramodules2.config.BalancingConfig;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.*;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(ExtraModules2.MODID)
public class EMModules {

	private static transient ArrayList<ResourceLocation> ITEM_REGISTRY_ORDER = new ArrayList<>();
	public static transient Map<BaseModule<?>, Item> moduleItemMap = new LinkedHashMap<>();
	private static transient CyclingItemGroup moduleGroup = new CyclingItemGroup("extramodules.modules", 40, () -> (Item[]) moduleItemMap.values().toArray((Object[]) new Item[0]), ITEM_REGISTRY_ORDER);

	@ObjectHolder("draconic_oxygen_storage")
	public static Module<OxygenStorageData> draconicOxygenStorage;
	@ObjectHolder("draconic_defense_brain")
	public static Module<DefenseBrainData> draconicDefenseBrain;
	@ObjectHolder("wyvern_defense_reflection")
	public static Module<DefenseSystemData> wyvernDefenseReflection;
	@ObjectHolder("draconic_defense_reflection")
	public static Module<DefenseSystemData> draconicDefenseReflection;
	@ObjectHolder("chaotic_defense_reflection")
	public static Module<DefenseSystemData> chaoticDefenseReflection;
	@ObjectHolder("draconic_potion_curer")
	public static Module<PotionCureData> draconicPotionCurer;
	@ObjectHolder("chaotic_generator")
	public static Module<GeneratorData> chaoticGenerator;
	@ObjectHolder("armor")
	public static Module<ArmorData> armor;

	private static Function<Module<OxygenStorageData>, OxygenStorageData> oxygenStorageData(int oxygenStorage) {
		return e -> {
			return new OxygenStorageData(oxygenStorage);
		};
	}

	private static Function<Module<DamageData>, DamageData> damageData(int damage) {
		return e -> {
			return new DamageData(damage);
		};
	}

	private static Function<Module<DefenseSystemData>, DefenseSystemData> defenseSystemData(int damage, int opCost, boolean odinsRage) {
		return e -> {
			return new DefenseSystemData(damage, opCost, odinsRage);
		};
	}

	private static Function<Module<DefenseSystemData>, DefenseSystemData> defenseSystemData(int damage, int opCost) {
		return e -> {
			return new DefenseSystemData(damage, opCost, false);
		};
	}

	private static Function<Module<DefenseBrainData>, DefenseBrainData> defenseBrain(int shootCooldown, int initialRageTicks, int rageModeTickCost, double rageModeRange) {
		return e -> {
			return new DefenseBrainData(shootCooldown, initialRageTicks, rageModeTickCost, rageModeRange);
		};
	}

	private static Function<Module<PotionCureData>, PotionCureData> potionCurer() {
		return e -> {
			return new PotionCureData();
		};
	}

	private static Function<Module<GeneratorData>, GeneratorData> generatorData(int opGeneration) {
		return e -> {
			return new GeneratorData(opGeneration);
		};
	}

	private static Function<Module<ArmorData>, ArmorData> armorData(int armor, int toughness) {
		return e -> {
			return new ArmorData(armor, toughness);
		};
	}


	private static Function<Module<ExtraHealthData>, ExtraHealthData> extraHealthData(double extraHealth) {
		return  e -> {
			return new ExtraHealthData(extraHealth);
		};
	}

	private static Function<Module<HitCooldownData>, HitCooldownData> hitCooldownData(int ticks) {
		return  e -> {
			return new HitCooldownData(ticks);
		};
	}

	private static Function<Module<EffectData>, EffectData> effectData(Effect effect, int amplifier, int tickCost) {
		return  e -> {
			return new EffectData(effect, amplifier, tickCost);
		};
	}

	public static void registerModules() {
		register(new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.DRACONIC, oxygenStorageData(BalancingConfig.DRACONIC_OXYGEN_STORAGE.get())), "draconic_oxygen_storage");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.WYVERN, defenseBrain(15, 100, BalancingConfig.WYVERN_RAGE_TICKCOST.get(), 6)), "wyvern_defense_brain");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.DRACONIC, defenseBrain(3, 150, BalancingConfig.DRACONIC_RAGE_TICKCOST.get(), 8)), "draconic_defense_brain");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.CHAOTIC, defenseBrain(1, 200, BalancingConfig.CHAOTIC_RAGE_TICKCOST.get(), 12)), "chaotic_defense_brain");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.WYVERN, defenseSystemData(2, 64000)), "wyvern_defense_reflection");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.DRACONIC, defenseSystemData(4, 128000)), "draconic_defense_reflection");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.CHAOTIC, defenseSystemData(10, 256000)), "chaotic_defense_reflection");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.DRACONIC, defenseSystemData(0, 10000000, true)).setMaxInstall(1), "draconic_odins_rage");
		register(new ModuleImpl<>(EMModuleTypes.POTION_CURER, TechLevel.DRACONIC, potionCurer()), "draconic_potion_curer");
		register(new ModuleImpl<>(EMModuleTypes.GENERATOR, TechLevel.DRACONIC, generatorData(BalancingConfig.DRACONIC_GENERATOR_PRODUCTION.get())), "draconic_generator");
		register(new ModuleImpl<>(EMModuleTypes.GENERATOR, TechLevel.CHAOTIC, generatorData(BalancingConfig.CHAOTIC_GENERATOR_PRODUCTION.get())), "chaotic_generator");
		register(new ModuleImpl<>(EMModuleTypes.ARMOR, TechLevel.WYVERN, armorData(1, 0)), "wyvern_armor");
		register(new ModuleImpl<>(EMModuleTypes.ARMOR, TechLevel.DRACONIC, armorData(2, 1)), "draconic_armor");
		register(new ModuleImpl<>(EMModuleTypes.ARMOR, TechLevel.CHAOTIC, armorData(4, 2)), "chaotic_armor");
		register(new ModuleImpl<>(EMModuleTypes.EXTRA_HEALTH, TechLevel.WYVERN, extraHealthData(5)), "wyvern_extra_health");
		register(new ModuleImpl<>(EMModuleTypes.EXTRA_HEALTH, TechLevel.DRACONIC, extraHealthData(20)), "draconic_extra_health");
		register(new ModuleImpl<>(EMModuleTypes.EXTRA_HEALTH, TechLevel.CHAOTIC, extraHealthData(40)), "chaotic_extra_health");
		register(new ModuleImpl<>(EMModuleTypes.HIT_COOLDOWN, TechLevel.DRACONIC, hitCooldownData(4)), "draconic_hit_cooldown");
		register(new ModuleImpl<>(EMModuleTypes.HIT_COOLDOWN, TechLevel.CHAOTIC, hitCooldownData(10)), "chaotic_hit_cooldown");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.WYVERN, effectData(Effects.REGENERATION,  0, 1000)), "wyvern_regeneration");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.REGENERATION,  1, 5000)), "draconic_regeneration");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC, effectData(Effects.REGENERATION,  3, 10000)), "chaotic_regeneration");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.DAMAGE_RESISTANCE,  0, 10000), 2, 1), "draconic_resistance");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC, effectData(Effects.DAMAGE_RESISTANCE,  1, 25000), 2, 1), "chaotic_resistance");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.ABSORPTION,  1, 5000), 2, 2), "draconic_absorption");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC, effectData(Effects.ABSORPTION,  4, 30000), 2, 2), "chaotic_absorption");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.FIRE_RESISTANCE,  0, 2000), 1, 1), "draconic_fire_resistance");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.LUCK, 0, 2000), 1, 1), "draconic_luck");

	}

	private static void register(ModuleImpl<?> module, String name) {
		ModuleItem<?> item = new ModuleItem((new Item.Properties()).tab(moduleGroup), module);
		item.setRegistryName(name + "_module");
		module.setRegistryName(name);
		module.setModuleItem(item);
		moduleItemMap.put(module, item);
	}


	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerItems(RegistryEvent.Register<Item> event) {
		moduleItemMap.clear();
		registerModules();
		moduleItemMap.keySet().forEach(BaseModule::reloadData);
		moduleItemMap.values().forEach(e -> event.getRegistry().register(e));
		ModuleCfg.saveStateConfig();
	}


	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerModules(RegistryEvent.Register<Module<?>> event) {
		moduleItemMap.keySet().forEach(e -> event.getRegistry().register(e));
	}

}
