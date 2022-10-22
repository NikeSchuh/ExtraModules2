package de.nike.extramodules2.tags;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class EMTags {


    public static class Items {

        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(ExtraModules2.MODID, name));
        }

        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }

    }

}
