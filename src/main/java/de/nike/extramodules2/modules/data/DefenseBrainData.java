package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class DefenseBrainData implements ModuleData<DefenseBrainData> {

	private final int shootCooldown;
	private final int initialRageTicks;
	private final int rageModeTickCost;
	private final double rageModeRange;

	public DefenseBrainData(int shootCooldown, int initialRageTicks, int rageModeTickCost, double rageModeRange) {
		this.shootCooldown = shootCooldown;
		this.initialRageTicks = initialRageTicks;
		this.rageModeTickCost = rageModeTickCost;
		this.rageModeRange = rageModeRange;
	}

	@Override
	public DefenseBrainData combine(DefenseBrainData other) {
		return new DefenseBrainData(shootCooldown + other.shootCooldown, initialRageTicks + other.initialRageTicks, rageModeTickCost + other.rageModeTickCost, rageModeRange + other.rageModeRange);
	}

	@Override
	public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {

	}

	public int getShootCooldown() {
		return shootCooldown;
	}

	public int getInitialRageTicks() {
		return initialRageTicks;
	}

	public int getRageModeTickCost() {
		return rageModeTickCost;
	}

	public double getRageModeRange() {
		return rageModeRange;
	}
}
