package de.nike.extramodules2.items.custom;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.items.ModularEnergyItem;
import de.nike.extramodules2.modules.EMModuleCategories;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.common.inventory.CurioSlot;

import javax.annotation.Nullable;
import java.util.Set;

public class EffectNecklace extends ModularEnergyItem{

    private int gridWidth, gridHeight;

    public EffectNecklace(TechPropBuilder builder, int gridWidth, int gridHeight) {
        super(builder);
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
    }

    @Override
    public ModuleHostImpl createHost(ItemStack stack) {
        ModuleHostImpl host = new ModuleHostImpl(tier, gridWidth, gridHeight, "necklace", false);
        host.addCategories(EMModuleCategories.EFFECT, ModuleCategory.ENERGY);
        return host;
    }

}
