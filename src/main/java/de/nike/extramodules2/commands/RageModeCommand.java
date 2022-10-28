package de.nike.extramodules2.commands;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.entities.EnergyEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;

import static com.brandon3055.draconicevolution.api.capability.DECapabilities.MODULE_HOST_CAPABILITY;

public class RageModeCommand {

    public RageModeCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("ragemode").executes(command -> {
            return ragemode(command.getSource());
        }));
    }

    private int ragemode(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getPlayerOrException();

        ItemStack chestpiece = ModularChestpiece.getChestpiece(playerEntity);
        LazyOptional<ModuleHost> lazyHost = chestpiece.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);

        lazyHost.ifPresent(host -> {
            DefenseBrainEntity entity = (DefenseBrainEntity) host.getEntitiesByType(EMModuleTypes.DEFENSE_BRAIN).findFirst().orElse(null);
            if(entity == null) return;
            entity.serverActivateRageMode(playerEntity);
            source.sendSuccess(new StringTextComponent(TextFormatting.GRAY + "Rage mode acivated!"), true);
        });

        return 1;
    }
}
