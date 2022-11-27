package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class GeneratorData implements ModuleData<GeneratorData> {

	private final int opGeneration;
	private final float efficiency;

	public GeneratorData(int opGeneration, float efficiency) {
		this.opGeneration = opGeneration;
		this.efficiency = efficiency;
	}

	@Override
	public GeneratorData combine(GeneratorData other) {
		return new GeneratorData(opGeneration + other.opGeneration, efficiency);
	}

	public int getOpGeneration() {
		return opGeneration;
	}
	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {

	}
}
