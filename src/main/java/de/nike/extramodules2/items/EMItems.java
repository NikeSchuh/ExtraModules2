package de.nike.extramodules2.items;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.items.custom.AntiPotionItem;
import de.nike.extramodules2.items.custom.EffectNecklace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class EMItems {

	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExtraModules2.MODID);

	public static final ItemGroup EXTRAMODULES_ITEMS = new ItemGroup("extramodules.items") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ANTI_POTION.get());
		}

	};

	public static final RegistryObject<Item> GENERATOR_FUEL = ITEMS.register("generator_fuel", () -> new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)) {
		@Override
		public int getBurnTime(ItemStack itemStack, @Nullable IRecipeType<?> recipeType) {
			return 256000;
		}
	});
	public static final RegistryObject<Item> ULTIMATE_GENERATOR_FUEL = ITEMS.register("ultimate_generator_fuel", () -> new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)) {
		@Override
		public int getBurnTime(ItemStack itemStack, @Nullable IRecipeType<?> recipeType) {
			return 2000000;
		}
	});
	public static final RegistryObject<Item> ADVANCED_MODULE_CORE = ITEMS.register("advanced_module_core", () -> new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));
	public static final RegistryObject<Item> SUPERIOR_MODULE_CORE = ITEMS.register("superior_module_core", () -> new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));
	public static final RegistryObject<Item> MODULE_CONTROLLER = ITEMS.register("module_controller", () -> new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));
	public static final RegistryObject<Item> ANTI_POTION = ITEMS.register("anti_potion", () -> new AntiPotionItem(new Item.Properties().tab(EXTRAMODULES_ITEMS).stacksTo(1)));
	public static final RegistryObject<Item> ELDER_GUARDIAN_PARTS = ITEMS.register("elder_guardian_parts", () -> new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));
	public static final RegistryObject<Item> ELDER_GUARDIAN_BRAIN = ITEMS.register("elder_guardian_brain", () -> new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));

	public static final RegistryObject<Item> WYVERN_EFFECT_NECKLACE = ITEMS.register("wyvern_effect_necklace", () -> new EffectNecklace(new TechPropBuilder(TechLevel.WYVERN).maxStackSize(1), 2, 2));
	public static final RegistryObject<Item> DRACONIC_EFFECT_NECKLACE = ITEMS.register("draconic_effect_necklace", () -> new EffectNecklace(new TechPropBuilder(TechLevel.DRACONIC).maxStackSize(1), 3, 3));
	public static final RegistryObject<Item> CHAOTIC_EFFECT_NECKLACE = ITEMS.register("chaotic_effect_necklace", () -> new EffectNecklace(new TechPropBuilder(TechLevel.CHAOTIC).maxStackSize(1), 4, 4));

	public static final RegistryObject<Item> WYVERN_PISTOL = ITEMS.register("wyvern_pistol", () -> new ModularPistol(new TechPropBuilder(TechLevel.WYVERN).maxStackSize(1), 4, 4, 10));
	public static final RegistryObject<Item> DRACONIC_PISTOL = ITEMS.register("draconic_pistol", () -> new ModularPistol(new TechPropBuilder(TechLevel.DRACONIC).maxStackSize(1), 8, 4, 15f));
	public static final RegistryObject<Item> CHAOTIC_PISTOL = ITEMS.register("chaotic_pistol", () -> new ModularPistol(new TechPropBuilder(TechLevel.CHAOTIC).maxStackSize(1), 10, 5, 20f));

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
