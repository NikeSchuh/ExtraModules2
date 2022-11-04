package de.nike.extramodules2.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import java.util.HashMap;

public class EffectCaps {

    private static HashMap<Effect, Integer> CAPS = new HashMap<>();

    static {
        CAPS.put(Effects.DAMAGE_RESISTANCE, 3);
    }

    public static boolean hasCap(Effect effect) {
        return CAPS.containsKey(effect);
    }

    public static int getCap(Effect effect) {
        return CAPS.get(effect);
    }

}
