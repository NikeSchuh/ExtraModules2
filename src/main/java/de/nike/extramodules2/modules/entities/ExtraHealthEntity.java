package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.BCContent;
import com.brandon3055.brandonscore.client.BCSprites;
import com.brandon3055.brandonscore.client.utils.GuiHelperOld;
import com.brandon3055.brandonscore.utils.BCProfiler;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.AutoFeedData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.ModuleTypes;
import de.nike.extramodules2.modules.data.ArmorData;
import de.nike.extramodules2.modules.data.ExtraHealthData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.UUID;

public class ExtraHealthEntity extends ModuleEntity {
    public ExtraHealthEntity(Module<ExtraHealthData> module) {
        super(module);
    }


    @Override
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        //IVertexBuilder builder = getter.getBuffer(BCSprites.GUI_TYPE);
       // ExtraHealthData data = (ExtraHealthData) module.getData();
        super.renderSlotOverlay(getter, mc, x, y, width, height, mouseX, mouseY, mouseOver, partialTicks);
    }

    @Override
    public void onInstalled(ModuleContext context) {
        if(!(context instanceof StackModuleContext)) return;
        StackModuleContext moduleContext = (StackModuleContext) context;
        if(!(moduleContext.getEntity() instanceof ServerPlayerEntity)) return;
        if(!moduleContext.isEquipped()) return;
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) moduleContext.getEntity();
        ExtraHealthData data = (ExtraHealthData) module.getData();
        addAttributes(playerEntity, data);
    }

    @Override
    public void onRemoved(ModuleContext context) {
        if(!(context instanceof StackModuleContext)) return;
        StackModuleContext moduleContext = (StackModuleContext) context;
        if(!(moduleContext.getEntity() instanceof ServerPlayerEntity)) return;
        if(!moduleContext.isEquipped()) return;
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) moduleContext.getEntity();
        ExtraHealthData data = (ExtraHealthData) module.getData();
        subAttributes(playerEntity, data);
    }

    public static void subAttributes(ServerPlayerEntity playerEntity, ExtraHealthData extraHealthData) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.randomUUID().toString(), -extraHealthData.getExtraHealth(), AttributeModifier.Operation.ADDITION));
        playerEntity.getAttributes().addTransientAttributeModifiers(map);
    }

    public static void addAttributes(ServerPlayerEntity playerEntity, ExtraHealthData extraHealthData) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.randomUUID().toString(), extraHealthData.getExtraHealth(), AttributeModifier.Operation.ADDITION));
        playerEntity.getAttributes().addTransientAttributeModifiers(map);
    }

    public static void equip(ServerPlayerEntity playerEntity, ModuleHost host) {
        ExtraHealthData extraHealthData = host.getModuleData(ModuleTypes.EXTRA_HEALTH, new ExtraHealthData(0));
        addAttributes(playerEntity, extraHealthData);
    }

    public static void unequip(ServerPlayerEntity playerEntity, ModuleHost host) {
        ExtraHealthData extraHealthData = host.getModuleData(ModuleTypes.EXTRA_HEALTH, new ExtraHealthData(0));
        subAttributes(playerEntity, extraHealthData);
    }

}
