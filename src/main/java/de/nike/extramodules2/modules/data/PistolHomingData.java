package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import javax.annotation.Nullable;
import java.util.Map;

public class PistolHomingData implements ModuleData<PistolHomingData> {

    @Override
    public void addInformation(Map map, @Nullable ModuleContext context, boolean stack) {

    }

    @Override
    public PistolHomingData combine(PistolHomingData other) {
        return new PistolHomingData();
    }
}
