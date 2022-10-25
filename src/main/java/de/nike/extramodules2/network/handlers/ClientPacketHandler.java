package de.nike.extramodules2.network.handlers;

import codechicken.lib.packet.ICustomPacketHandler;
import codechicken.lib.packet.PacketCustom;
import de.nike.extramodules2.modules.entities.HitCooldownEntitiy;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import de.nike.extramodules2.network.EMNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;

public class ClientPacketHandler implements ICustomPacketHandler.IClientPacketHandler {
	@Override
	public void handlePacket(PacketCustom packet, Minecraft mc, IClientPlayNetHandler handler) {
		switch (packet.getType()) {
		case EMNetwork.S_EYE_MODE_CHANGE:
			DefenseBrainEntity.modeChange(packet.readVarInt());
			break;
			case EMNetwork.S_EYE_RAGE_CHARGE:
				DefenseBrainEntity.rageModeCharge(packet.readFloat());
				break;
				case EMNetwork.S_HIT_COOLDOWN:
					HitCooldownEntitiy.clientTicksUpdate(packet.readVarInt());
					break;
		}
	}
}
