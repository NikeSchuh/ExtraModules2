package de.nike.extramodules2.items;

import com.brandon3055.brandonscore.client.utils.CyclingItemGroup;
import de.nike.extramodules2.ExtraModules2;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EMItems {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExtraModules2.MODID);

    public static final ItemGroup EXTRAMODULES_ITEMS = new ItemGroup("extraModulesTab") {
        @Override
        public ItemStack makeIcon()  {
            return new ItemStack(GENERATOR_FUEL.get());
        }

    };


    public static final RegistryObject<Item> GENERATOR_FUEL =
            ITEMS.register("generator_fuel", () ->new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));
    public static final RegistryObject<Item> ADVANCED_MODULE_CORE =
            ITEMS.register("advanced_module_core", () ->new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));
    public static final RegistryObject<Item> SUPERIOR_MODULE_CORE =
            ITEMS.register("superior_module_core", () ->new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));
    public static final RegistryObject<Item> MODULE_CONTROLLER =
            ITEMS.register("module_controller", () ->new Item(new Item.Properties().tab(EXTRAMODULES_ITEMS)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
