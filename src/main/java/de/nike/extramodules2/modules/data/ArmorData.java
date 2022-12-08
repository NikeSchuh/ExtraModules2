package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class ArmorData implements ModuleData<ArmorData>{

    private final int armor;
    private final int toughness;

    public ArmorData(int armor, int toughness) {
        this.armor = armor;
        this.toughness = toughness;
    }

    @Override
    public ArmorData combine(ArmorData other) {
        return new ArmorData(other.armor + armor, toughness + other.toughness);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, @Nullable ModuleContext context, boolean stack) {

    }

    public int getArmor() {
        return armor;
    }

    public int getToughness() {
        return toughness;
    }

}
