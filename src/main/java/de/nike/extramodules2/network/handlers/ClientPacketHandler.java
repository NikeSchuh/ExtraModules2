package de.nike.extramodules2.network.handlers;

import codechicken.lib.packet.ICustomPacketHandler;
import codechicken.lib.packet.PacketCustom;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import de.nike.extramodules2.network.EMNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;

public class ClientPacketHandler implements ICustomPacketHandler.IClientPacketHandler{
    @Override
    public void handlePacket(PacketCustom packet, Minecraft mc, IClientPlayNetHandler handler) {
        System.out.println(packet.getType());
        switch (packet.getType()) {
            case EMNetwork.S_EYE_MODE_CHANGE:
                DefenseBrainEntity.modeChange(packet.readVarInt());
                break;
        }
    }
}
