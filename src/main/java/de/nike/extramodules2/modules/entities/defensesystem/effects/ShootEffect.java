package de.nike.extramodules2.modules.entities.defensesystem.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ShootEffect {

    @OnlyIn(Dist.CLIENT)
    public static void playEffect(Vector3d pos1, Vector3d pos2) {
        ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
        ClientWorld clientWorld = playerEntity.clientLevel;
        clientWorld.playLocalSound(new BlockPos(pos1.x, pos1.y, pos1.z), SoundEvents.GUARDIAN_HURT, SoundCategory.PLAYERS, 10f, (float) (2f - Math.random()), true);
        drawLine(pos1.add(0, 2, 0), pos2, 0.5D, clientWorld);
    }

    public static void drawLine(Vector3d point1, Vector3d point2, double space, ClientWorld world
    ) {

        double distance = point1.distanceTo(point2);
        Vector3d vector = point2.subtract(point1).normalize().multiply(space, space ,space);
        double covered = 0;
        for (; covered < distance; point1 = point1.add(vector)) {
            world.addParticle(ParticleTypes.SOUL, point1.x, point1.y, point1.z, Math.random() * 0.05, 0.05D, Math.random() * 0.05);
            covered += space;
        }
    }

}
