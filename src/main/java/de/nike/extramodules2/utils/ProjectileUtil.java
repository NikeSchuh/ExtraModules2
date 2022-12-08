package de.nike.extramodules2.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.antlr.v4.runtime.atn.PredicateEvalInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ProjectileUtil {

    public static EntityRayTraceResult getTargetedEntity(PlayerEntity player) {
        Vector3d start = player.getEyePosition(player.getEyeHeight(player.getPose()));
        Vector3d addition = player.getLookAngle();
        return ProjectileHelper.getEntityHitResult(
                player.level,
                player,
                start,
                start.add(addition),
                player.getBoundingBox().expandTowards(addition).inflate(1.0),
                Entity::isAttackable);
    }

    public static EntityRayTraceResult getFirstEntityHit(World level, Entity origin, Vector3d start, Vector3d direction, int samples, AxisAlignedBB box, Predicate<Entity> entityPredicate) {
        Vector3d current = start;
        for(int i = 0; i < samples; i++) {
            current = current.add(direction);
            for(Entity entity : level.getEntities(origin, new AxisAlignedBB(current.x + box.maxX, current.y + box.maxY, current.z + box.maxZ, current.x + box.minX, current.y + box.minY, current.z + box.minZ))) {
                if(entityPredicate.test(entity)) {
                    return new EntityRayTraceResult(entity);
                }
            }
        }
        return null;
    }

}
