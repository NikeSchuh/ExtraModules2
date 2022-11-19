package de.nike.extramodules2.integration.curios;

import com.brandon3055.draconicevolution.integration.equipment.CuriosIntegration;
import de.nike.extramodules2.items.EMItems;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.function.Function;

public class EMCuriosIntegration {

    public static final Tags.IOptionalNamedTag<Item> NECKLACE_TAG = ItemTags.createOptional(new ResourceLocation("curios", "necklace"));

    private static void sendImc(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
    }

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EMCuriosIntegration::sendImc);
    }

}
