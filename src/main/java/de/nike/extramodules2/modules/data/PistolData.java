package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Map;

public class PistolData implements ModuleData<PistolData> {

    private static final DecimalFormat format = new DecimalFormat("#.##");

    private final float criticalChance;

    public PistolData(float chance) {
        criticalChance = chance;
    }

    @Override
    public PistolData combine(PistolData other) {
        return new PistolData(criticalChance + other.criticalChance);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.extramodules2.pistol.crit"), TranslationUtils.string( format.format(criticalChance * 100) + "%"));
    }

    public float getCriticalChance() {
        return criticalChance > 1 ? 1 : criticalChance;
    }
}
