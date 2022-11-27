package de.nike.extramodules2.entity.projectiles;

import com.brandon3055.draconicevolution.entity.projectile.DraconicArrowEntity;
import com.brandon3055.draconicevolution.init.DEContent;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.entity.EMEntities;
import de.nike.extramodules2.network.EMNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class DraconicBulletEntity extends AbstractArrowEntity {

    private static final DataParameter<Integer> LIGHTNING_COLOR = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.INT);
    private static final DataParameter<Float> PENETRATION = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> CRITICAL_CHANCE = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);

    private List<Entity> piercedEntities = new ArrayList<>();

    public DraconicBulletEntity(EntityType<? extends DraconicBulletEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public DraconicBulletEntity(World world, double xPos, double yPos, double zPos) {
        super(EMEntities.DRACONIC_BULLET, xPos, yPos, zPos, world);
        setNoGravity(true);
    }

    public DraconicBulletEntity(World world, LivingEntity shooter) {
        super(EMEntities.DRACONIC_BULLET, shooter, world);
        setNoGravity(true);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
            if(!entity.level.isClientSide) {
                if(!piercedEntities.contains(entity)) {
                    if(level.random.nextFloat() < getPenetration()) {
                        piercedEntities.add(result.getEntity());
                        setPenetration(getPenetration()-0.25f);
                    } else {
                        remove();
                    }
                    if(level.random.nextFloat() < getCriticalChance()) {
                        DraconicLightningChainEntity chainEntity = new DraconicLightningChainEntity(entity.level, getOwner(), getOwner(), entity);
                        chainEntity.setLightningColor(getLightningColor());
                        chainEntity.setDamage((float) getBaseDamage());
                        chainEntity.setLightningSegments(Math.min((int) (4 + (Math.pow(getBaseDamage() / 4, 2) / 5)), 60));
                        chainEntity.setLightningSize(Math.min(30, (int) (1 + (getBaseDamage() / 4))));
                        chainEntity.moveTo(entity.position().add(0, 3, 0));
                        entity.level.addFreshEntity(chainEntity);
                        EMNetwork.sendLightningChain(entity.level.dimension(), entity.blockPosition(), 100f, getOwner(), getOwner(), entity, chainEntity);
                        entity.hurt(new IndirectEntityDamageSource("arrow", this, getOwner()).setProjectile(), (float) getBaseDamage() * 4);
                    }
                    entity.hurt(new IndirectEntityDamageSource("arrow", this, getOwner()).setProjectile(), (float) getBaseDamage());
                }
            }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        pickup = PickupStatus.DISALLOWED;

    }

    public void tick() {
        super.tick();
        if(tickCount > 200) remove();
        if(inGroundTime > 20) remove();
       /*/ if(level.isClientSide) {
            Vector3d smokePos = position().subtract(getDeltaMovement().multiply(2, 2 ,2));
            level.addParticle(ParticleTypes.SMOKE, smokePos.x, smokePos.y, smokePos.z, 0 , 0, 0);
        }/*/
    }

    public void setLightningColor(int color) {
        entityData.set(LIGHTNING_COLOR, color);
    }

    public int getLightningColor() {
        return entityData.get(LIGHTNING_COLOR);
    }

    public void setPenetration(float value) {
        entityData.set(PENETRATION, value);
    }

    public float getPenetration() {
        return entityData.get(PENETRATION);
    }

    public void setCriticalChance(float chance) {
        entityData.set(CRITICAL_CHANCE, chance);
    }

    public float getCriticalChance() {
        return entityData.get(CRITICAL_CHANCE);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(LIGHTNING_COLOR, 0);
        entityData.define(PENETRATION, 0.0f);
        entityData.define(CRITICAL_CHANCE, 0.0f);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return EMNetwork.getSpawnPacket(this);
    }

}
