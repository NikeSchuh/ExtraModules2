package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class HitCooldownData implements ModuleData<HitCooldownData> {

    private final int hitCooldown;

    public HitCooldownData(int hitCooldown) {
        this.hitCooldown = hitCooldown;
    }

    @Override
    public HitCooldownData combine(HitCooldownData other) {
        return new HitCooldownData(hitCooldown + other.hitCooldown);
    }

    public int getHitCooldownTicks() {
        return hitCooldown;
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {

    }
}
