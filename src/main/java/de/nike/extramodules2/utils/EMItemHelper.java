package de.nike.extramodules2.utils;

import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class EMItemHelper {

    public static boolean isModularChestPieceEquipped(LivingEntity livingEntity) {
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlotType.CHEST);
        if(stack.getItem() instanceof ModularChestpiece) return true;
        return CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof ModularChestpiece, livingEntity).isPresent();
    }


}
