package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class DefenseBrainData implements ModuleData<DefenseBrainData> {

	public DefenseBrainData() {

	}

	@Override
	public DefenseBrainData combine(DefenseBrainData other) {
		return new DefenseBrainData();
	}

	@Override
	public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {

	}

}
