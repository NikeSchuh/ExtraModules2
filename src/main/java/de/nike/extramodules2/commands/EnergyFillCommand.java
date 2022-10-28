package de.nike.extramodules2.commands;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;

public class EnergyFillCommand {

    public EnergyFillCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("fillenergy").executes(command -> {
            return fillenergy(command.getSource());
        }));
    }

    private int fillenergy(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getPlayerOrException();

        ItemStack chestpiece = ModularChestpiece.getChestpiece(playerEntity);
        LazyOptional<ModuleHost> lazyHost = chestpiece.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);

        lazyHost.ifPresent(host -> {
            StackModuleContext context = new StackModuleContext(chestpiece, playerEntity, chestpiece.getEquipmentSlot()).setInEquipModSlot(true);
            IOPStorageModifiable storage =context.getOpStorage();
            storage.modifyEnergyStored(storage.getMaxEnergyStored());
            source.sendSuccess(new StringTextComponent(TextFormatting.GRAY + "Rage mode acivated!"), true);
        });

        return 1;
    }

}
