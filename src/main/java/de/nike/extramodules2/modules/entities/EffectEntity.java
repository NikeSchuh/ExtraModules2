package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import de.nike.extramodules2.modules.data.EffectData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class EffectEntity extends ModuleEntity {

    private boolean active = false;

    public EffectEntity(Module<EffectData> module) {
        super(module);
    }

    @Override
    public void tick(ModuleContext context) {
        if(EffectiveSide.get().isClient()) return;
        if(context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            if(isEquipped(moduleContext)) {
                LivingEntity entity = moduleContext.getEntity();
                IOPStorageModifiable modifiable = moduleContext.getOpStorage();
                EffectData data = (EffectData) module.getData();

                if (moduleContext != null) {
                    if (modifiable.getEnergyStored() > data.getTickCost()) {
                        modifiable.modifyEnergyStored(-data.getTickCost());
                        active = true;
                    } else active = false;
                }

                if (active) {
                    if (entity.tickCount % data.getApplyDelay() == 0) {
                        entity.addEffect(new EffectInstance(data.getMobEffect(), data.getApplyDelay() + 1, data.getAmplifier()));
                    }
                }
            }
        }
    }

    public boolean isEquipped(StackModuleContext context) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> curios = CuriosApi.getCuriosHelper().findEquippedCurio(context.getItem().getItem(), context.getEntity());
        AtomicBoolean equipped = new AtomicBoolean(false);
        curios.ifPresent(stringIntegerItemStackImmutableTriple -> {
            ItemStack stack = stringIntegerItemStackImmutableTriple.right;
            if(stack.equals(context.getStack())) equipped.set(true);
        });
        return equipped.get();
    }
}
