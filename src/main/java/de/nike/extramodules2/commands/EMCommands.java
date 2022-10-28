package de.nike.extramodules2.commands;

import de.nike.extramodules2.ExtraModules2;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = ExtraModules2.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EMCommands {

    public static RageModeCommand rageModeCommand;

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
            //rageModeCommand = new RageModeCommand(event.getDispatcher());
            // new EnergyFillCommand(event.getDispatcher());
            //ConfigCommand.register(event.getDispatcher());
    }


}
