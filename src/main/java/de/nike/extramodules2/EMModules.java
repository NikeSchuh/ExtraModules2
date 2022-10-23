package de.nike.extramodules2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.client.utils.CyclingItemGroup;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.DamageData;
import com.brandon3055.draconicevolution.api.modules.lib.BaseModule;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleImpl;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleItem;
import com.brandon3055.draconicevolution.init.ModuleCfg;

import de.nike.extramodules2.modules.ModuleTypes;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.data.DefenseSystemData;
import de.nike.extramodules2.modules.data.GeneratorData;
import de.nike.extramodules2.modules.data.OxygenStorageData;
import de.nike.extramodules2.modules.data.PotionCureData;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
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

	private static Function<Module<DefenseBrainData>, DefenseBrainData> defenseBrain() {
		return e -> {
			return new DefenseBrainData();
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

	public static void registerModules() {
		register(new ModuleImpl<>(ModuleTypes.OXYGEN_STORAGE, TechLevel.DRACONIC, oxygenStorageData(2000)), "draconic_oxygen_storage");
		register(new ModuleImpl<>(ModuleTypes.DEFENSE_BRAIN, TechLevel.DRACONIC, defenseBrain()), "draconic_defense_brain");
		register(new ModuleImpl<>(ModuleTypes.DEFENSE_SYSTEM, TechLevel.WYVERN, defenseSystemData(2, 64000)), "wyvern_defense_reflection");
		register(new ModuleImpl<>(ModuleTypes.DEFENSE_SYSTEM, TechLevel.DRACONIC, defenseSystemData(4, 128000)), "draconic_defense_reflection");
		register(new ModuleImpl<>(ModuleTypes.DEFENSE_SYSTEM, TechLevel.CHAOTIC, defenseSystemData(10, 256000)), "chaotic_defense_reflection");
		register(new ModuleImpl<>(ModuleTypes.DEFENSE_SYSTEM, TechLevel.DRACONIC, defenseSystemData(0, 10000000, true)), "draconic_odins_rage");
		register(new ModuleImpl<>(ModuleTypes.POTION_CURER, TechLevel.DRACONIC, potionCurer()), "draconic_potion_curer");
		register(new ModuleImpl<>(ModuleTypes.GENERATOR, TechLevel.CHAOTIC, generatorData(12600)), "chaotic_generator");
		register(new ModuleImpl<>(ModuleTypes.GENERATOR, TechLevel.DRACONIC, generatorData(2000)), "draconic_generator");

	}

	private static void register(ModuleImpl<?> module, String name) {
		ModuleItem<?> item = new ModuleItem((new Item.Properties()).tab(moduleGroup), (Module) module);
		item.setRegistryName(name + "_module");
		module.setRegistryName(name);
		module.setModuleItem((Item) item);
		moduleItemMap.put(module, item);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		moduleItemMap.clear();
		registerModules();
		moduleItemMap.keySet().forEach(BaseModule::reloadData);
		moduleItemMap.values().forEach(e -> event.getRegistry().register(e));
		ModuleCfg.saveStateConfig();
	}

	@SubscribeEvent
	public static void registerModules(RegistryEvent.Register<Module<?>> event) {
		moduleItemMap.keySet().forEach(e -> event.getRegistry().register(e));
	}

}
