package de.nike.extramodules2.loot;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class DataProvider extends GlobalLootModifierProvider {

    public DataProvider(DataGenerator gen, String modid) {
        super(gen, modid);
    }

    @Override
    protected void start() {
        add("guardian_drop", EMLootModifiers.GUARDIAN_DROPS.get(), new GuardianDropAdderModifier(
                new ILootCondition[]{KilledByPlayer.killedByPlayer().invert().build(),
                        EntityHasProperty.hasProperties(LootContext.EntityTarget.KILLER,
                                EntityPredicate.Builder.entity().entityType(
                                        EntityTypePredicate.of(EntityType.ELDER_GUARDIAN)).build()).build()}
        ));
    }
}
