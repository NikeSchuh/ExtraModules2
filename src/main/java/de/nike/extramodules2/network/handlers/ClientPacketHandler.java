package de.nike.extramodules2.network.handlers;

import codechicken.lib.packet.ICustomPacketHandler;
import codechicken.lib.packet.PacketCustom;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.client.entity.ChainData;
import de.nike.extramodules2.client.entity.EntityDataAdder;
import de.nike.extramodules2.entity.projectiles.DraconicLightningChainEntity;
import de.nike.extramodules2.modules.entities.HitCooldownEntitiy;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import de.nike.extramodules2.modules.entities.defensesystem.effects.ShootEffect;
import de.nike.extramodules2.network.EMNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

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
					case EMNetwork.S_EYE_SHOOT_EFFECT:
						ShootEffect.playEffect(packet.readVec3d(), packet.readVec3d());
						break;
						case EMNetwork.S_ADD_ENTITY:
							handleEntitySpawn(packet, mc);
							break;
							case EMNetwork.S_LIGHTNING_CHAIN:
								handleLightningChain(packet, mc);
								break;
		}
	}

	private static void handleLightningChain(PacketCustom packetCustom, Minecraft mc) {
		ClientWorld world = mc.level;
		if(world==null) return;
		Logger logger = ExtraModules2.LOGGER;

		int ownerID = packetCustom.readInt();
		int originID = packetCustom.readInt();
		int targetID = packetCustom.readInt();

		int lightningEntityID = packetCustom.readInt();


		Entity owner = world.getEntity(ownerID);
		Entity origin = world.getEntity(originID);
		Entity target = world.getEntity(targetID);
		ChainData chainData = new ChainData(lightningEntityID, owner, origin, target);

		EntityDataAdder.chains.put(lightningEntityID, chainData);
	}


	private static void handleEntitySpawn(PacketCustom packet, Minecraft mc) {
		if (mc.level == null) {
			return;
		}
		EntityType<?> type = Registry.ENTITY_TYPE.byId(packet.readVarInt());
		int entityID = packet.readInt();
		UUID uuid = packet.readUUID();
		double posX = packet.readDouble();
		double posY = packet.readDouble();
		double posZ = packet.readDouble();
		byte yaw = packet.readByte();
		byte pitch = packet.readByte();
		byte headYaw = packet.readByte();
		Vector3d velocity = new Vector3d(packet.readFloat(), packet.readFloat(), packet.readFloat());
		Entity entity = type.create(mc.level);
		if (entity == null) {
			return;
		}
		entity.setPacketCoordinates(posX, posY, posZ);
		entity.absMoveTo(posX, posY, posZ, (pitch * 360) / 256.0F, (yaw * 360) / 256.0F);
		entity.setYHeadRot((headYaw * 360) / 256.0F);
		entity.setYBodyRot((headYaw * 360) / 256.0F);
		entity.setId(entityID);
		entity.setUUID(uuid);
		mc.level.putNonPlayerEntity(entityID, entity);
		entity.lerpMotion(velocity.x, velocity.y, velocity.z);
	}
}
