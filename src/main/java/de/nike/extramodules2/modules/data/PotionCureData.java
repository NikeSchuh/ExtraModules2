package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class PotionCureData implements ModuleData<PotionCureData> {
    @Override
    public PotionCureData combine(PotionCureData other) {
        return new PotionCureData();
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {

    }
}
