package de.nike.extramodules2.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.nike.extramodules2.items.EMItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class GuardianBrainAdditionModifier extends LootModifier {

    public Item addition;

    protected GuardianBrainAdditionModifier(ILootCondition[] conditionsIn, Item addition) {
        super(conditionsIn);
        this.addition = addition;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(addition, 1));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GuardianBrainAdditionModifier> {

        @Override
        public GuardianBrainAdditionModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new GuardianBrainAdditionModifier(ailootcondition, ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(object, "addition"))));
        }

        @Override
        public JsonObject write(GuardianBrainAdditionModifier instance) {
            JsonObject jsonObject = makeConditions(instance.conditions);
            jsonObject.addProperty("addition", ForgeRegistries.ITEMS.getKey(instance.addition).toString());
            return jsonObject;
        }
    }
}
