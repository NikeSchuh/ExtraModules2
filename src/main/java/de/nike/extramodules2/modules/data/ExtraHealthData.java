package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class ExtraHealthData implements ModuleData<ExtraHealthData> {

    private final double extraHealth;

    public ExtraHealthData(double extraHealth) {
        this.extraHealth = extraHealth;
    }

    public double getExtraHealth() {
        return extraHealth;
    }

    @Override
    public ExtraHealthData combine(ExtraHealthData other) {
        return new ExtraHealthData(extraHealth + other.extraHealth);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.extramodules2.extra_health.extra_health"), new StringTextComponent("" + extraHealth));
    }
}
