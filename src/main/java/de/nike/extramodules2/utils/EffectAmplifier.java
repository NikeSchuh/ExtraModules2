package de.nike.extramodules2.utils;

import net.minecraft.potion.Effect;

public class EffectAmplifier {

    private final Effect effect;
    private final int amplifier;

    public EffectAmplifier(Effect effect, int amplifier) {
        this.effect = effect;
        this.amplifier = amplifier;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
