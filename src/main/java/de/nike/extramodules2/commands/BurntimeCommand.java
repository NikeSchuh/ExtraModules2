package de.nike.extramodules2.commands;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.http.util.TextUtils;

public class BurntimeCommand {

    public BurntimeCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("burntime").executes(command -> {
            return burntime(command.getSource());
        }));
    }

    private int burntime(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getPlayerOrException();
        ItemStack stack = playerEntity.getMainHandItem();

        playerEntity.sendMessage(TranslationUtils.string(ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING) + ""), null);
        playerEntity.sendMessage(TranslationUtils.string(stack.getItem().getBurnTime(stack, IRecipeType.SMELTING) + ""), null);
        return 1;
    }

}
