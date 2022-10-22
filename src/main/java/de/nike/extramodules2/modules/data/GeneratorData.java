package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class GeneratorData implements ModuleData<GeneratorData> {

    private final int opGeneration;

    public GeneratorData(int opGeneration) {
        this.opGeneration = opGeneration;
    }

    @Override
    public GeneratorData combine(GeneratorData other) {
        return new GeneratorData(opGeneration + other.opGeneration);
    }

    public int getOpGeneration() {
        return opGeneration;
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.extramodules2.generator.generation"), new StringTextComponent("" + opGeneration + " OP/t"));
    }
}
