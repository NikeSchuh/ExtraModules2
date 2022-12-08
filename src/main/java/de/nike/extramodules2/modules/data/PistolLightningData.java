package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;

public class PistolLightningData implements ModuleData<PistolLightningData> {

    private final int range;
    private final float chance;
    private final float surroundingDamage;
    private final int chargeTickCost;
    private final int chargeTicks;

    public PistolLightningData(int range, float chance, float surroundingDamage, int chargeTickCost, int chargeTicks) {
        this.range = range;
        this.chance = chance;
        this.surroundingDamage = surroundingDamage;
        this.chargeTickCost = chargeTickCost;
        this.chargeTicks = chargeTicks;
    }

    public int getRange() {
        return range;
    }

    public float getChance() {
        return chance;
    }

    public float getSurroundingDamage() {
        return surroundingDamage;
    }

    public int getChargeTickCost() {
        return chargeTickCost;
    }

    public int getChargeTicks() {
        return chargeTicks;
    }

    @Override
    public PistolLightningData combine(PistolLightningData other) {
        return new PistolLightningData(range + other.range, chance + other.chance, surroundingDamage + other.surroundingDamage,  chargeTickCost + other.chargeTickCost, chargeTicks + other.chargeTicks);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {

    }
}
