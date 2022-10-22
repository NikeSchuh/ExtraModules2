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
    private final boolean odinsRage;

    public DefenseSystemData(int reflectedDamage, int opCost, boolean odinsRage) {
        this.reflectedDamage = reflectedDamage;
        this.opCost = opCost;
        this.odinsRage = odinsRage;
    }

    @Override
    public DefenseSystemData combine(DefenseSystemData other) {
        return new DefenseSystemData(reflectedDamage + other.reflectedDamage, opCost + other.opCost, Boolean.logicalOr(odinsRage, other.odinsRage));
    }


    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {
        if (reflectedDamage > 0) {
            map.put(new TranslationTextComponent("module.extramodules2.defense_brain.reflected_damage"), new StringTextComponent("+" + reflectedDamage));
        }
        map.put(new TranslationTextComponent("module.extramodules2.defense_brain.defense_op_cost"), new StringTextComponent(opCost + " OP"));
    }

    public int getReflectedDamage() {
        return reflectedDamage;
    }

    public int getOpCost() {
        return opCost;
    }

    public boolean isOdinsRage() {
        return odinsRage;
    }
}
