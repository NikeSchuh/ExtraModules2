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
    private final float criticalDamage;
    private final float extraBaseDamage;
    private final int fireTicks;

    public PistolData(float criticalChance, float criticalDamage, float extraBaseDamage, int fireTicks) {
        this.criticalChance = criticalChance;
        this.criticalDamage = criticalDamage;
        this.extraBaseDamage = extraBaseDamage;
        this.fireTicks = fireTicks;
    }

    @Override
    public PistolData combine(PistolData other) {
        return new PistolData(criticalChance + other.criticalChance, criticalDamage + other.getCriticalDamage(), extraBaseDamage + other.extraBaseDamage, fireTicks + other.fireTicks);
    }

    public float getCriticalDamage() {
        return criticalDamage;
    }

    public float getExtraBaseDamage() {
        return extraBaseDamage;
    }

    public int getFireTicks() {
        return fireTicks;
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
       if(criticalChance > 0)  map.put(new TranslationTextComponent("module.extramodules2.pistol.crit"), TranslationUtils.string( format.format(criticalChance * 100) + "%"));
       if(criticalDamage > 0)  map.put(new TranslationTextComponent("module.extramodules2.pistol.critdmg"), TranslationUtils.string( format.format(criticalDamage * 100) + "%"));
       if(extraBaseDamage > 0) map.put(new TranslationTextComponent("module.extramodules2.pistol.basedmg"), TranslationUtils.string("+"+ format.format(extraBaseDamage)));
       if(fireTicks > 0 ) map.put(new TranslationTextComponent("module.extramodules2.pistol.firetime"), TranslationUtils.string( format.format(fireTicks / 20F) + "s"));
    }

    public float getCriticalChance() {
        return criticalChance > 1 ? 1 : criticalChance;
    }
}
