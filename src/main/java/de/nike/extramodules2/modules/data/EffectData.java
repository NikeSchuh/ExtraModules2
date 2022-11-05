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

    private final HashMap<Effect, Integer> ampMap;
    private final int tickCost;

    public EffectData(Effect mobEffect, int amplifier, int tickCost) {
        this.ampMap =new HashMap<>();
        ampMap.put(mobEffect, amplifier + 1);
        this.tickCost = tickCost;
    }

    public EffectData(HashMap<Effect, Integer> ampMap, int tickCost) {
        this.ampMap = ampMap;
        this.tickCost = tickCost;
    }


    public int getTickCost() {
        return tickCost;
    }

    public HashMap<Effect, Integer> getAmpMap() {
        return ampMap;
    }

    @Override
    public EffectData combine(EffectData other) {
        HashMap<Effect, Integer> combinedMap = new HashMap();
        combinedMap.putAll(ampMap);
        for(Effect effect : other.getAmpMap().keySet()) {
            combinedMap.put(effect, combinedMap.getOrDefault(effect, 0) + other.ampMap.get(effect));
        }
        return new EffectData(combinedMap, tickCost + other.tickCost);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.extramodules2.effect.tickcost"), TranslationUtils.string(FormatUtils.formatE(tickCost) + " OP/t"));
        for(Effect effect : ampMap.keySet()) {
            int amp = ampMap.get(effect) - 1;
            if(EffectCaps.hasCap(effect)) amp = Math.min(amp, EffectCaps.getCap(effect));
            map.put(TranslationUtils.string(FormatUtils.capitalizeString(effect.getRegistryName().getPath().replace("_", " "))), TranslationUtils.string(FormatUtils.toRoman(amp + 1)));
        }
        if(stack) {
            ampMap.keySet().stream().findFirst().ifPresent(localEffect -> {
                if(EffectCaps.hasCap(localEffect))
                map.put(new TranslationTextComponent("module.extramodules2.effect.levelcap"), TranslationUtils.string(FormatUtils.toRoman(EffectCaps.getCap(localEffect) + 1)));
            });
        }
        }
}
