package de.nike.extramodules2.modules.entities;

import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.ExtraHealthData;
import de.nike.extramodules2.utils.EMItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;
import java.util.stream.Collectors;

public class ExtraHealthEntity extends ModuleEntity {

    public static final String ATTR_HEALTH = "2790fa48-5c5f-11ed-9b6a-0242ac120002";

    public ExtraHealthEntity(Module<ExtraHealthData> module) {
        super(module);
    }

    @Override
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        super.renderSlotOverlay(getter, mc, x, y, width, height, mouseX, mouseY, mouseOver, partialTicks);
    }

    @Override
    public void onInstalled(ModuleContext context) {
        if(!(context instanceof StackModuleContext)) return;
        StackModuleContext moduleContext = (StackModuleContext) context;
        if(!(moduleContext.getEntity() instanceof ServerPlayerEntity)) return;
        if(!EMItemHelper.isModularChestPieceEquipped(moduleContext.getEntity())) return;
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) moduleContext.getEntity();
        ExtraHealthData data = (ExtraHealthData) module.getData();
        addAttributes(playerEntity, data);
    }

    @Override
    public void onRemoved(ModuleContext context) {
        if(!(context instanceof StackModuleContext)) return;
        StackModuleContext moduleContext = (StackModuleContext) context;
        if(!(moduleContext.getEntity() instanceof ServerPlayerEntity)) return;
        if(!EMItemHelper.isModularChestPieceEquipped(moduleContext.getEntity())) return;
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) moduleContext.getEntity();
        subAttributes(playerEntity, (ExtraHealthData) module.getData());
    }

    public static void subAttributes(ServerPlayerEntity playerEntity, ExtraHealthData extraHealthData) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.MAX_HEALTH, new AttributeModifier(ATTR_HEALTH, -extraHealthData.getExtraHealth(), AttributeModifier.Operation.ADDITION));
        playerEntity.getAttributes().addTransientAttributeModifiers(map);

    }

    public static void addAttributes(ServerPlayerEntity playerEntity, ExtraHealthData extraHealthData) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.MAX_HEALTH, new AttributeModifier(ATTR_HEALTH, extraHealthData.getExtraHealth(), AttributeModifier.Operation.ADDITION));
        playerEntity.getAttributes().addTransientAttributeModifiers(map);
    }

    public static void equip(ServerPlayerEntity playerEntity, ModuleHost host) {
        ExtraHealthData extraHealthData = host.getModuleData(EMModuleTypes.EXTRA_HEALTH, new ExtraHealthData(0));
        addAttributes(playerEntity, extraHealthData);
    }

    public static void unequip(ServerPlayerEntity playerEntity, ModuleHost host) {
        ExtraHealthData extraHealthData = host.getModuleData(EMModuleTypes.EXTRA_HEALTH, new ExtraHealthData(0));
        subAttributes(playerEntity, extraHealthData);
    }

}
