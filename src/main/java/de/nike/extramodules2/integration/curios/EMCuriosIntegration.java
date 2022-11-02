package de.nike.extramodules2.integration.curios;

import com.brandon3055.draconicevolution.integration.equipment.CuriosIntegration;
import de.nike.extramodules2.items.EMItems;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Function;

public class EMCuriosIntegration extends CuriosIntegration {

    public static final Tags.IOptionalNamedTag<Item> CHARM_TAG = ItemTags.createOptional(new ResourceLocation("curios", "charm"));

    public static void generateTags(Function<Tags.IOptionalNamedTag<Item>, TagsProvider.Builder<Item>> builder) {
        builder.apply(CHARM_TAG).add(EMItems.WYVERN_EFFECT_NECKLACE.get());
    }

}
