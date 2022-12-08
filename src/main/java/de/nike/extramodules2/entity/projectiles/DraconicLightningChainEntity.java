package de.nike.extramodules2.entity.projectiles;

import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.damagesources.DraconicLightningDamageSource;
import de.nike.extramodules2.entity.EMEntities;
import de.nike.extramodules2.network.EMNetwork;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import org.lwjgl.system.CallbackI;

import java.util.concurrent.ThreadLocalRandom;

public class DraconicLightningChainEntity extends Entity {

    private DraconicBulletEntity bulletParent;

    private static final DataParameter<Integer> LIGHTNING_COLOR = EntityDataManager.defineId(DraconicLightningChainEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> LIGHTNING_SIZE = EntityDataManager.defineId(DraconicLightningChainEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> LIGHTNING_SEGMENTS = EntityDataManager.defineId(DraconicLightningChainEntity.class, DataSerializers.INT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.defineId(DraconicLightningChainEntity.class, DataSerializers.FLOAT);

    private int liveTime = 14;

    private Entity owner;
    private Entity target;
    private Entity origin;

    private float progression = 1.0f;
    private boolean finished = false;
    private long lightningSeed = ThreadLocalRandom.current().nextInt(0, 10000000);

    public DraconicLightningChainEntity(World world) {
        super(EMEntities.DRACONIC_CHAIN, world);
    }
    public DraconicLightningChainEntity(EntityType<? extends DraconicLightningChainEntity> entityType, World world) {
        super(entityType, world);
    }

    public DraconicLightningChainEntity(World world, Entity owner, Entity origin, Entity target) {
        super(EMEntities.DRACONIC_CHAIN, world);
        setOwner(owner);
        setOrigin(origin);
        setTarget(target);
    }

    public DraconicLightningChainEntity(World world, double xPos, double yPos, double zPos) {
        super(EMEntities.DRACONIC_CHAIN, world);
        setPos(xPos, yPos, zPos);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        double d0 = 64.0D * getViewScale();
        return p_70112_1_ < d0 * d0;
    }

    public void setParent(DraconicBulletEntity parent) {
        this.bulletParent = parent;
    }

    public void setLightningColor(int color) {
        entityData.set(LIGHTNING_COLOR, color);
    }

    public void setLightningSize(int size) {
        entityData.set(LIGHTNING_SIZE, size);
    }

    public void setLightningSegments(int segments) {
        entityData.set(LIGHTNING_SEGMENTS, segments);
    }

    public int getLightningColor() {
        return entityData.get(LIGHTNING_COLOR);
    }

    public int getLightningSize() {
        return entityData.get(LIGHTNING_SIZE);
    }

    public int getLightningSegments() {
        return entityData.get(LIGHTNING_SEGMENTS);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(LIGHTNING_COLOR, 0);
        entityData.define(LIGHTNING_SEGMENTS, 2);
        entityData.define(LIGHTNING_SIZE, 3);
        entityData.define(DAMAGE, 0f);
    }

    public void setDamage(float damage) {
        entityData.set(DAMAGE, damage);
    }

    public float getDamage() {
        return entityData.get(DAMAGE);
    }

    @Override
    public void tick() {
        super.tick();
        if(getOrigin() == null || getTarget() == null) {
            return;
        }
        if(!level.isClientSide) {
            liveTime--;
            if(liveTime <= 0) {
                remove();
            }
        }

        if(level.isClientSide) {
            if(tickCount % 2 == 0) {
                lightningSeed++;
            }
        }

        progression = Math.max(0, progression - 0.2f);

        if(!finished) {
            if (progression <= 0) {
                finished = true;
                onStrike();
            }
        }
    }

    public long getLightningSeed() {
        return lightningSeed;
    }

    public void onStrike() {
        level.playSound(null, blockPosition(), EMSounds.distantThunder, SoundCategory.WEATHER, 10f, 0.5f + (level.random.nextFloat() / 2));
        if(!level.isClientSide) {
            BlockPos targetPos = getTarget().blockPosition();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(targetPos.offset(bulletParent.getLightningSize(),bulletParent.getLightningSize(),bulletParent.getLightningSize()), targetPos.offset(-bulletParent.getLightningSize(), -bulletParent.getLightningSize(), -bulletParent.getLightningSize()));
            if(getOwner() instanceof  PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) getOwner();
                for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB)) {
                    if(livingEntity.equals(playerEntity)) continue;
                    livingEntity.setRemainingFireTicks(bulletParent.getFireTicks());
                    livingEntity.hurt(new DraconicLightningDamageSource(playerEntity, this), getDamage());
                }
            }
        }
    }

    public float getProgression() {
        return progression;
    }

    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getOrigin() {
        return origin;
    }

    public void setOrigin(Entity origin) {
        this.origin = origin;
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {

    }


    public int getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }


    @Override
    public IPacket<?> getAddEntityPacket() {
        return EMNetwork.getSpawnPacket(this);
    }

}
