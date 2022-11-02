package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import de.nike.extramodules2.utils.EffectAmplifier;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.potion.Effect;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.Map;

public class EffectData implements ModuleData<EffectData> {

    private final EffectAmplifier[] effects;
    private final Effect mobEffect;
    private final int amplifier;
    private final int tickCost;
    private final int applyDelay;

    public EffectData(Effect mobEffect, int amplifier, int tickCost, int applyDelay) {
        this.applyDelay = applyDelay;
        this.effects = new EffectAmplifier[] {new EffectAmplifier(mobEffect, amplifier)};
        this.mobEffect = mobEffect;
        this.tickCost = tickCost;
        this.amplifier = amplifier;
    }


    private EffectData(EffectAmplifier[] effects, int tickCost) {
        this.effects = effects;
        this.tickCost = tickCost;
        this.applyDelay = 0;
        this.mobEffect = null;
        this.amplifier = 0;
    }

    public Effect getMobEffect() {
        return mobEffect;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getTickCost() {
        return tickCost;
    }

    public int getApplyDelay() {
        return applyDelay;
    }

    public EffectAmplifier[] getEffects() {
        return effects;
    }

    @Override
    public EffectData combine(EffectData other) {
        return new EffectData(ArrayUtils.addAll(getEffects(), other.getEffects()), tickCost + other.tickCost);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(TranslationUtils.string("Cost"), TranslationUtils.string(FormatUtils.formatE(tickCost) + " OP/t"));
        for(int i = 0; i < getEffects().length; i++) {
            EffectAmplifier amplifier = getEffects()[i];
            map.put(TranslationUtils.string(FormatUtils.capitalizeString(amplifier.getEffect().getRegistryName().getPath())), TranslationUtils.string(FormatUtils.toRoman(amplifier.getAmplifier())));
        }
    }
}
