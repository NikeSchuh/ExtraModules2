package de.nike.extramodules2.network;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustomChannelBuilder;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.network.handlers.ClientPacketHandler;
import de.nike.extramodules2.network.handlers.ServerPacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

public class EMNetwork {

	public static final ResourceLocation CHANNEL = new ResourceLocation(ExtraModules2.MODID + ":emnetwork");
	public static EventNetworkChannel networkChannel;

	// Server -> Client
	public static final int S_EYE_MODE_CHANGE = 1;
	public static final int S_EYE_RAGE_CHARGE = 2;

	public static void sendEyeChangeMode(ServerPlayerEntity target, int mode) {
		PacketCustom packetCustom = new PacketCustom(CHANNEL, S_EYE_MODE_CHANGE);
		packetCustom.writeVarInt(mode);
		packetCustom.sendToPlayer(target);
	}

	public static void sendEyeRageCharge(ServerPlayerEntity target, float ticks) {
		PacketCustom packetCustom = new PacketCustom(CHANNEL, S_EYE_RAGE_CHARGE);
		packetCustom.writeFloat(ticks);
		packetCustom.sendToPlayer(target);
	}

	public static void init() {
		networkChannel = PacketCustomChannelBuilder.named(CHANNEL).networkProtocolVersion(() -> "1")//
				.clientAcceptedVersions(e -> true)//
				.serverAcceptedVersions(e -> true)//
				.assignClientHandler(() -> ClientPacketHandler::new)//
				.assignServerHandler(() -> ServerPacketHandler::new)//
				.build();
	}

}
