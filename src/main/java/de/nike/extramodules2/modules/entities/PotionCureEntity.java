package de.nike.extramodules2.modules.entities;

import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import de.nike.extramodules2.modules.data.PotionCureData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class PotionCureEntity extends ModuleEntity {

    private BooleanProperty activated;

    public PotionCureEntity(Module<PotionCureData> module) {
        super(module);
        addProperty(activated  = new BooleanProperty("potion_mod.remove_potions", true).setFormatter(ConfigProperty.BooleanFormatter.ENABLED_DISABLED));
        this.savePropertiesToItem = true;
    }

    @Override
    public void tick(ModuleContext context) {
        if(context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            if(!moduleContext.isEquipped()) return;
            if(!activated.getValue()) return;
            LivingEntity entity = moduleContext.getEntity();
            if (entity instanceof ServerPlayerEntity && entity.tickCount % 10 == 0 && ((StackModuleContext) context).isEquipped()) {
                ServerPlayerEntity playerEntity = (ServerPlayerEntity) entity;
                for(EffectInstance instance : playerEntity.getActiveEffects().toArray(new EffectInstance[playerEntity.getActiveEffects().size()])) {
                    Effect effect = instance.getEffect();
                    if(!effect.isBeneficial()) {
                        playerEntity.removeEffect(effect);
                    }
                }
            }
        }
    }
}
