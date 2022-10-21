package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class DefenseSystemData implements ModuleData<DefenseSystemData> {

    private final int reflectedDamage;
    private final int opCost;

    public DefenseSystemData(int reflectedDamage, int opCost) {
        this.reflectedDamage = reflectedDamage;
        this.opCost = opCost;
    }

    @Override
    public DefenseSystemData combine(DefenseSystemData other) {
        return new DefenseSystemData(reflectedDamage + other.reflectedDamage, opCost + other.opCost);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.extramodules2.defense_brain.reflected_damage"), new StringTextComponent("+" + reflectedDamage));
        map.put(new TranslationTextComponent("module.extramodules2.defense_brain.defense_op_cost"), new StringTextComponent(opCost + " OP"));
    }

    public int getReflectedDamage() {
        return reflectedDamage;
    }

    public int getOpCost() {
        return opCost;
    }
}
