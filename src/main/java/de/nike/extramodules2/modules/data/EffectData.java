package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.effects.EffectCaps;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.potion.Effect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EffectData implements ModuleData<EffectData> {

    private final Effect[] effects;
    private final int[] amplifiers;
    private final int[] delays;
    private final int tickCost;

    public EffectData(Effect mobEffect, int amplifier, int tickCost, int applyDelay) {
        this.delays = new int[] {applyDelay};
        this.effects = new Effect[] {mobEffect};
        this.amplifiers = new int[] {amplifier};
        this.tickCost = tickCost;
    }

    public EffectData(Effect[] effects, int[] amplifiers, int tickCost, int[] delays) {
        this.effects = effects;
        this.amplifiers = amplifiers;
        this.tickCost = tickCost;
        this.delays = delays;
    }


    public int[] getAmplifiers() {
        return amplifiers;
    }

    public int getTickCost() {
        return tickCost;
    }

    public int[] getDelays() {
        return delays;
    }

    public Effect[] getEffects() {
        return effects;
    }



    @Override
    public EffectData combine(EffectData other) {
        return new EffectData(ArrayUtils.addAll(getEffects(), other.getEffects()), ArrayUtils.addAll(getAmplifiers(), other.getAmplifiers()), tickCost + other.tickCost, ArrayUtils.addAll(getDelays(), other.getDelays()));
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.extramodules2.effect.tickcost"), TranslationUtils.string(FormatUtils.formatE(tickCost) + " OP/t"));
        HashMap<Effect, Integer> effectMap = new HashMap();
        for(int i = 0; i < effects.length; i++) {
            Effect effect = effects[i];
            int amp = amplifiers[i] + 1;
            effectMap.put(effect, effectMap.getOrDefault(effect, 0) + amp);
        }
        for(Effect effect : effectMap.keySet()) {
            if(EffectCaps.hasCap(effect)) {
                map.put(TranslationUtils.string(FormatUtils.capitalizeString(effect.getRegistryName().getPath())), TranslationUtils.string(FormatUtils.toRoman(Math.min(effectMap.get(effect), EffectCaps.getCap(effect)))));
            } else
            map.put(TranslationUtils.string(FormatUtils.capitalizeString(effect.getRegistryName().getPath())), TranslationUtils.string(FormatUtils.toRoman(effectMap.get(effect))));
        }
        if(stack) {
            if(EffectCaps.hasCap(effects[0])) {
                map.put(new TranslationTextComponent("module.extramodules2.effect.levelcap"), TranslationUtils.string(EffectCaps.getCap(effects[0]) + ""));
            }
        }
    }
}
