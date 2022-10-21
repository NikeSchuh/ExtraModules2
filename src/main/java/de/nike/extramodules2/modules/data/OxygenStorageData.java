package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class OxygenStorageData implements ModuleData<OxygenStorageData> {

    private final int oxygenStored;

    public OxygenStorageData(int oxygenStored) {
        this.oxygenStored = oxygenStored;
    }

    @Override
    public OxygenStorageData combine(OxygenStorageData other) {
        return new OxygenStorageData(this.oxygenStored + other.oxygenStored);
    }

    public int getOxygenStored() {
        return oxygenStored;
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.extramodules2.oxygen_storage.oxygen_storage"), new StringTextComponent(oxygenStored + ""));
    }
}
