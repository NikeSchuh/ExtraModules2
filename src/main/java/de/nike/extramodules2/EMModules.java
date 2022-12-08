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

	private static Function<Module<GeneratorData>, GeneratorData> generatorData(int opGeneration, float efficiency) {
		return e -> {
			return new GeneratorData(opGeneration, efficiency);
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

	private static Function<Module<PistolData>, PistolData> pistolData(float critChance, float criticalDamage, float extraBaseDamage, int fireTicks)
	{
		return e ->{
			return new PistolData(critChance, criticalDamage, extraBaseDamage, fireTicks);
		};
	}

	private static Function<Module<PistolLightningData>, PistolLightningData> pistolLightningData(float chance, int size, float damageConversion, int chargeTickCost, int chargeTicks)
	{
		return e ->{
			return new PistolLightningData(size, chance, damageConversion, chargeTickCost, chargeTicks);
		};
	}

	private static Function<Module<PistolHomingData>, PistolHomingData> pistolHomingData()
	{
		return e ->{
			return new PistolHomingData();
		};
	}


	public static void registerModules() {
		register(new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.WYVERN, oxygenStorageData(BalancingConfig.WYVERN_OXYGEN_STORAGE.get())), "wyvern_oxygen_storage");
		register(new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.DRACONIC, oxygenStorageData(BalancingConfig.DRACONIC_OXYGEN_STORAGE.get())), "draconic_oxygen_storage");
		register(new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.CHAOTIC, oxygenStorageData(BalancingConfig.CHAOTIC_OXYGEN_STORAGE.get())), "chaotic_oxygen_storage");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.WYVERN, defenseBrain(15, 100, BalancingConfig.WYVERN_RAGE_TICKCOST.get(), 6)), "wyvern_defense_brain");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.DRACONIC, defenseBrain(3, 150, BalancingConfig.DRACONIC_RAGE_TICKCOST.get(), 8)), "draconic_defense_brain");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.CHAOTIC, defenseBrain(1, 200, BalancingConfig.CHAOTIC_RAGE_TICKCOST.get(), 12)), "chaotic_defense_brain");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.WYVERN, defenseSystemData(2, 64000)), "wyvern_defense_reflection");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.DRACONIC, defenseSystemData(4, 128000)), "draconic_defense_reflection");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.CHAOTIC, defenseSystemData(10, 256000)), "chaotic_defense_reflection");
		register(new ModuleImpl<>(EMModuleTypes.DEFENSE_SYSTEM, TechLevel.DRACONIC, defenseSystemData(0, 10000000, true)).setMaxInstall(1), "draconic_odins_rage");
		register(new ModuleImpl<>(EMModuleTypes.POTION_CURER, TechLevel.DRACONIC, potionCurer()), "draconic_potion_curer");
		register(new ModuleImpl<>(EMModuleTypes.GENERATOR, TechLevel.DRACONIC, generatorData(BalancingConfig.DRACONIC_GENERATOR_PRODUCTION.get(), 4.0f)), "draconic_generator");
		register(new ModuleImpl<>(EMModuleTypes.GENERATOR, TechLevel.CHAOTIC, generatorData(BalancingConfig.CHAOTIC_GENERATOR_PRODUCTION.get(), 12.0f)), "chaotic_generator");
		register(new ModuleImpl<>(EMModuleTypes.ARMOR, TechLevel.WYVERN, armorData(1, 0)), "wyvern_armor");
		register(new ModuleImpl<>(EMModuleTypes.ARMOR, TechLevel.DRACONIC, armorData(2, 1)), "draconic_armor");
		register(new ModuleImpl<>(EMModuleTypes.ARMOR, TechLevel.CHAOTIC, armorData(4, 2)), "chaotic_armor");
		register(new ModuleImpl<>(EMModuleTypes.EXTRA_HEALTH, TechLevel.WYVERN, extraHealthData(5)), "wyvern_extra_health");
		register(new ModuleImpl<>(EMModuleTypes.EXTRA_HEALTH, TechLevel.DRACONIC, extraHealthData(20)), "draconic_extra_health");
		register(new ModuleImpl<>(EMModuleTypes.EXTRA_HEALTH, TechLevel.CHAOTIC, extraHealthData(40)), "chaotic_extra_health");
		register(new ModuleImpl<>(EMModuleTypes.HIT_COOLDOWN, TechLevel.DRACONIC, hitCooldownData(2)), "draconic_hit_cooldown");
		register(new ModuleImpl<>(EMModuleTypes.HIT_COOLDOWN, TechLevel.CHAOTIC, hitCooldownData(5)), "chaotic_hit_cooldown");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.WYVERN, effectData(Effects.REGENERATION,  0, 250)), "wyvern_regeneration");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.REGENERATION,  1, 500)), "draconic_regeneration");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC, effectData(Effects.REGENERATION,  3, 1000)), "chaotic_regeneration");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.DAMAGE_RESISTANCE,  0, 2500), 2, 1), "draconic_resistance");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC, effectData(Effects.DAMAGE_RESISTANCE,  1, 6000), 2, 1), "chaotic_resistance");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.ABSORPTION,  1, 3000), 2, 2), "draconic_absorption");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC, effectData(Effects.ABSORPTION,  4, 5000), 2, 2), "chaotic_absorption");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.FIRE_RESISTANCE,  0, 300), 1, 1), "draconic_fire_resistance");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.LUCK, 0, 1000), 1, 1), "draconic_luck");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.WYVERN, effectData(Effects.DAMAGE_BOOST, 0, 300), 1, 1), "wyvern_strength");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.DAMAGE_BOOST, 2, 1000), 1, 2), "draconic_strength");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC, effectData(Effects.DAMAGE_BOOST, 3, 2000), 1, 2), "chaotic_strength");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.INVISIBILITY, 0, 2500), 2, 2), "draconic_invisibility");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.WYVERN, effectData(Effects.DIG_SPEED, 0, 400), 1, 1), "wyvern_haste");
		register(new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC, effectData(Effects.DIG_SPEED, 1, 1200), 1, 1), "draconic_haste");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.WYVERN, pistolData(0.05f, 0.0f, 0.0f, 0), 2, 1), "wyvern_crit");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.DRACONIC, pistolData(0.1f, 0.0f, 0.0f, 0), 2, 1), "draconic_crit");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.CHAOTIC, pistolData(0.2f, 0.0f, 0.0f, 0), 2, 1), "chaotic_crit");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.WYVERN, pistolData(0.0f, 0.1f, 0.5f, 0), 1, 1), "wyvern_crit_dmg");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.DRACONIC, pistolData(0.0f, 0.25f, 1.0f, 0), 1, 1), "draconic_crit_dmg");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.CHAOTIC, pistolData(0.0f, 0.50f, 2.0f, 0), 1, 1), "chaotic_crit_dmg");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.WYVERN, pistolData(0.0f, 0, 0.5f, 20), 1, 1), "wyvern_fire");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.DRACONIC, pistolData(0.0f, 0, 1f, 40), 1, 1), "draconic_fire");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.CHAOTIC, pistolData(0.0f, 0, 1.25f, 80), 1, 1), "chaotic_fire");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL_LIGHTNING, TechLevel.WYVERN, pistolLightningData(0.1F, 4, 0.5F, 500, 2000)), "wyvern_lightning");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL_LIGHTNING, TechLevel.DRACONIC, pistolLightningData(0.2F, 8, 0.75F, 10000, 1000)), "draconic_lightning");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL_LIGHTNING, TechLevel.CHAOTIC, pistolLightningData(0.3F, 12, 1.0F, 200000, 500), 1, 3), "chaotic_lightning");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL_HOMING, TechLevel.DRACONIC, pistolHomingData(), 3, 3), "draconic_homing");
		register(new ModuleImpl<>(EMModuleTypes.PISTOL_HOMING, TechLevel.CHAOTIC, pistolHomingData(), 2, 2), "chaotic_homing");
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
