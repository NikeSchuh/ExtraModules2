package de.nike.extramodules2.items.custom;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.api.power.OPStorage;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.effects.EffectCaps;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.items.ModularEnergyItem;
import de.nike.extramodules2.modules.EMModuleCategories;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.EffectData;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.common.inventory.CurioSlot;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EffectNecklace extends ModularEnergyItem implements ICurioItem{

    private int gridWidth, gridHeight;
    private boolean active = false;

    public EffectNecklace(TechPropBuilder builder, int gridWidth, int gridHeight) {
        super(builder);
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
    }

    @Override
    public ModuleHostImpl createHost(ItemStack stack) {
        ModuleHostImpl host = new ModuleHostImpl(tier, gridWidth, gridHeight, "necklace", false);
        host.addCategories(EMModuleCategories.EFFECT, ModuleCategory.ENERGY);
        return host;
    }


    @Override
    public void equipmentTick(ItemStack stack, LivingEntity livingEntity) {
        if(EffectiveSide.get().isClient()) return;
        LazyOptional<ModuleHost> moduleHost = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
        LazyOptional<IOPStorage> opStorage = stack.getCapability(DECapabilities.OP_STORAGE);
        if(moduleHost.isPresent() && opStorage.isPresent()) {
            ModuleHost host = moduleHost.orElse(null);
            IOPStorageModifiable storage = (IOPStorageModifiable) opStorage.orElse(null);
            EffectData effectData = host.getModuleData(EMModuleTypes.EFFECT);
            if(effectData != null) {
                int opCost = effectData.getTickCost();
                if(storage.getOPStored() > opCost) {
                    storage.modifyEnergyStored(-opCost);
                    active = true;
                } else active = false;
                if(active) {
                    HashMap<Effect, Integer> effectMap = new HashMap();
                    HashMap<Effect, Integer> delayMap = new HashMap<>();
                    for(int i = 0; i < effectData.getEffects().length; i++) {
                        Effect effect = effectData.getEffects()[i];
                        int amp =  effectData.getAmplifiers()[i];
                        effectMap.put(effect, effectMap.getOrDefault(effect, 0) + amp);
                        delayMap.put(effect, effectData.getDelays()[i]);
                    }
                    int i = 0;
                    for(Effect effect : effectMap.keySet()) {
                        int app = delayMap.get(effect);
                        if(livingEntity.tickCount % app == 0) {
                            int amp = effectMap.get(effect);
                            if (EffectCaps.hasCap(effect)) {
                                amp = Math.min(amp, EffectCaps.getCap(effect));
                            }
                            livingEntity.addEffect(new EffectInstance(effect, app + 1, amp));
                        }
                        i++;
                    }
                }
            }
        }
    }

    @Override
    public List<ITextComponent> getTagsTooltip(ItemStack stack, List<ITextComponent> tagTooltips) {
        return super.getTagsTooltip(stack, tagTooltips);
    }

    @Override
    public boolean canRightClickEquip(ItemStack stack) {
        return super.canRightClickEquip(stack);
    }

    @Override
    public boolean canEquip(LivingEntity livingEntity, String slotID) {
        return super.canEquip(livingEntity, slotID);
    }
}
