package de.nike.extramodules2.loot;

import com.google.gson.JsonObject;
import de.nike.extramodules2.items.EMItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class GuardianDropAdderModifier extends LootModifier {

    public GuardianDropAdderModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);

    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(EMItems.ELDER_GUARDIAN_BRAIN.get()));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GuardianDropAdderModifier> {

        @Override
        public GuardianDropAdderModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new GuardianDropAdderModifier(conditionsIn);
        }


        @Override
        public JsonObject write(GuardianDropAdderModifier instance) {
            return makeConditions(instance.conditions);
        }


    }
}
