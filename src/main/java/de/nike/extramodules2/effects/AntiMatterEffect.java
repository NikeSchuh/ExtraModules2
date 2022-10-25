package de.nike.extramodules2.effects;

import de.nike.extramodules2.damagesources.EMDamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.awt.*;

public class AntiMatterEffect extends Effect {

    public static final int COLOR = new Color(255, 255, 255).getRGB();

    protected AntiMatterEffect() {
        super(EffectType.HARMFUL, COLOR);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(!livingEntity.level.isClientSide) {
            livingEntity.hurt(EMDamageSources.ANTI_MATTER, 10 * (amplifier + 1));
        }
        super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int tick, int tick2) {
        return tick % 20 == 0;
    }
}
