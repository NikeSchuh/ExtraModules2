package de.nike.extramodules2.modules.entities;

import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.ArmorData;
import de.nike.extramodules2.utils.EMItemHelper;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.datafix.fixes.FurnaceRecipes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.UUID;

public class ArmorEntity extends ModuleEntity {

    public ArmorEntity(Module<ArmorData> module) {
        super(module);
    }

    @Override
    public void onInstalled(ModuleContext context) {
        if(!(context instanceof StackModuleContext)) return;
        StackModuleContext moduleContext = (StackModuleContext) context;
        if(!(moduleContext.getEntity() instanceof ServerPlayerEntity)) return;
        if(!EMItemHelper.isModularChestPieceEquipped(moduleContext.getEntity())) return;
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) moduleContext.getEntity();
        ArmorData data = (ArmorData) module.getData();
        addAttributes(playerEntity, data);
    }

    @Override
    public void onRemoved(ModuleContext context) {
        if(!(context instanceof StackModuleContext)) return;
        StackModuleContext moduleContext = (StackModuleContext) context;
        if(!(moduleContext.getEntity() instanceof ServerPlayerEntity)) return;
        if(!EMItemHelper.isModularChestPieceEquipped(moduleContext.getEntity())) return;
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) moduleContext.getEntity();
        ArmorData data = (ArmorData) module.getData();
        subAttributes(playerEntity, data);
    }

    @Override
    public void addToolTip(List<ITextComponent> list) {
        ArmorData armorData = (ArmorData) module.getData();
        if(armorData.getArmor() > 0)
            list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.armor.armor") + ": " + TextFormatting.GREEN + "" + armorData.getArmor()));
        if(armorData.getToughness() > 0)
            list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.armor.armor_toughness") + ": " + TextFormatting.GREEN + "" + armorData.getToughness()));
    }

    public static void subAttributes(ServerPlayerEntity playerEntity, ArmorData armorData) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.ARMOR, new AttributeModifier(UUID.randomUUID().toString(), -armorData.getArmor(), AttributeModifier.Operation.ADDITION));
        map.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID().toString(), -armorData.getToughness(), AttributeModifier.Operation.ADDITION));
        playerEntity.getAttributes().addTransientAttributeModifiers(map);
    }

    public static void addAttributes(ServerPlayerEntity playerEntity, ArmorData armorData) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.ARMOR, new AttributeModifier(UUID.randomUUID().toString(), armorData.getArmor(), AttributeModifier.Operation.ADDITION));
        map.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID().toString(), armorData.getToughness(), AttributeModifier.Operation.ADDITION));
        playerEntity.getAttributes().addTransientAttributeModifiers(map);
    }

    public static void equip(ServerPlayerEntity playerEntity, ModuleHost host) {
        ArmorData armorData = host.getModuleData(EMModuleTypes.ARMOR, new ArmorData(0, 0));
        addAttributes(playerEntity, armorData);
    }

    public static void unequip(ServerPlayerEntity playerEntity, ModuleHost host) {
        ArmorData armorData = host.getModuleData(EMModuleTypes.ARMOR, new ArmorData(0, 0));
        subAttributes(playerEntity, armorData);
    }





}
