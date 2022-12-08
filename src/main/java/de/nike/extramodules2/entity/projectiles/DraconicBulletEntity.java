package de.nike.extramodules2.entity.projectiles;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.api.damage.DraconicIndirectEntityDamage;
import com.brandon3055.draconicevolution.entity.projectile.DraconicArrowEntity;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.lib.ProjectileAntiImmunityDamage;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.entity.EMEntities;
import de.nike.extramodules2.modules.entities.PistolLightningEntity;
import de.nike.extramodules2.network.EMNetwork;
import de.nike.extramodules2.utils.NikesMath;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DraconicBulletEntity extends AbstractArrowEntity {

    private static final DataParameter<Integer> LIGHTNING_COLOR = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.INT);
    private static final DataParameter<Float> PENETRATION = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> CRITICAL_CHANCE = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> LIGHTNING_CHANCE = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> LIGHTNING_DAMAGE_SIZE = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.INT);
    private static final DataParameter<Float> LIGHTNING_DAMAGE_CONVERSION = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> CRIT_DAMAGE_MULT = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> PROJ_ANTI_IMMUNE = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Byte> TECH_LEVEL = EntityDataManager.defineId(DraconicBulletEntity.class,DataSerializers.BYTE);
    private static final DataParameter<Integer> FIRE_TICKS = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.INT);

    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.INT);
    private static final DataParameter<Float> SPEED_MULT = EntityDataManager.defineId(DraconicBulletEntity.class, DataSerializers.FLOAT);

    private PistolLightningEntity pistolLightningEntity;
    private List<Entity> piercedEntities = new ArrayList<>();
    private boolean targetLost = false;

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

    public boolean isIgnoringImmunityCancellation() {
        return entityData.get(PROJ_ANTI_IMMUNE);
    }

    public void setIgnoreCancellation(boolean lean) {
       entityData.set(PROJ_ANTI_IMMUNE, lean);
    }

    public void setPistolLightningEntity(PistolLightningEntity pistolLightningEntity) {
        this.pistolLightningEntity = pistolLightningEntity;
    }

    public void setTarget(Entity target) {
        if(!(target == null))
        entityData.set(TARGET_ENTITY, target.getId());
    }

    public void setTarget(int id) {
        entityData.set(TARGET_ENTITY, id);
    }

    public Entity getTarget() {
        if(entityData.get(TARGET_ENTITY) == 0) return null;
        return level.getEntity(entityData.get(TARGET_ENTITY));
    }

    public void setSpeedMult(float mult) {
        entityData.set(SPEED_MULT, mult);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
            if(!entity.level.isClientSide) {
                setTarget(0);
                if(!piercedEntities.contains(entity)) {
                    if(level.random.nextFloat() < getPenetration()) {
                        piercedEntities.add(result.getEntity());
                        setPenetration(getPenetration()-0.25f);
                    } else {
                        remove();
                    }

                    DamageSource source = getDamageSource(entity);

                    boolean crit = level.random.nextFloat() < getCriticalChance();
                    double damage = getBaseDamage();
                    if(crit) damage *= getCritDamageMult();

                    // Lightning

                    if(pistolLightningEntity != null && pistolLightningEntity.isCharged() && level.random.nextFloat() < getLightningChance()) {
                        DraconicLightningChainEntity chainEntity = new DraconicLightningChainEntity(entity.level, getOwner(), getOwner(), entity);
                        chainEntity.setLightningColor(getLightningColor());
                        chainEntity.setDamage((float) (damage * getLightningDamageConversion()));
                        chainEntity.setParent(this);
                        chainEntity.setLightningSegments(Math.min((int) (10 + (Math.pow(damage / 4, 2) / 5)), 60));
                        chainEntity.setLightningSize(Math.min(60, (int) (3 + (damage / 4))));
                        chainEntity.moveTo(entity.position().add(0, 3, 0));
                        entity.level.addFreshEntity(chainEntity);
                        EMNetwork.sendLightningChain(entity.level.dimension(), entity.blockPosition(), 100f, getOwner(), getOwner(), entity, chainEntity);
                        pistolLightningEntity.setCharge(0);
                   }

                    if(getFireTicks() > 0) {
                        entity.setSecondsOnFire(getFireTicks() / 20);
                    }
                    damage(entity, source, (float) damage);
                }
            }
    }

    public DamageSource getDamageSource(Entity target) {
        Entity owner = this.getOwner();
        EntityDamageSource damagesource;
        TechLevel techLevel = TechLevel.byIndex(entityData.get(TECH_LEVEL));
        if (owner == null) {
            damagesource = DraconicIndirectEntityDamage.arrow(this, this, techLevel);
        } else {
            damagesource = DraconicIndirectEntityDamage.arrow(this, owner, techLevel);
            if (owner instanceof LivingEntity) {
                ((LivingEntity) owner).setLastHurtMob(target);
            }
        }
        if (isIgnoringImmunityCancellation() && DEConfig.projectileAntiImmuneEntities.contains(target.getType().getRegistryName().toString())) {
            damagesource = new ProjectileAntiImmunityDamage("arrow", this, damagesource.getEntity(), techLevel);
        }
        return damagesource;
    }

    private void damage(Entity entity, DamageSource source, float amount) {
        entity.hurt(source, amount);
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
        if(!targetLost) {
            Entity target = getTarget();
            if(target == null) {
                targetLost = true;
                return;
            }
            Vector3d dir = target.getEyePosition(target.getEyeHeight()).subtract(position()).normalize();
            float speedMult = getSpeedMult() / 2;
            setDeltaMovement(dir.multiply(speedMult, speedMult, speedMult));
        }
    }

    public float getSpeedMult() {
        return entityData.get(SPEED_MULT);
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

    public float getLightningChance() {
        return entityData.get(LIGHTNING_CHANCE);
    }

    public void setLightningChance(float chance) {
        entityData.set(LIGHTNING_CHANCE, chance);
    }

    public void setCritDamageMult(float mult) {
        entityData.set(CRIT_DAMAGE_MULT, mult);
    }

    public int getFireTicks() {
        return entityData.get(FIRE_TICKS);
    }

    public void setFireTicks(int ticks) {
        entityData.set(FIRE_TICKS, ticks);
    }

    public float getCritDamageMult() {
        return entityData.get(CRIT_DAMAGE_MULT);
    }


    public void setTechLevel(TechLevel techLevel) {
        entityData.set(TECH_LEVEL,(byte) techLevel.index);
    }

    public void setLightningDamageSize(int size) {
        entityData.set(LIGHTNING_DAMAGE_SIZE, size);
    }

    public void setLightningDamageConversion(float conversion) {
        entityData.set(LIGHTNING_DAMAGE_CONVERSION, conversion);
    }

    public float getLightningDamageConversion() {
        return entityData.get(LIGHTNING_DAMAGE_CONVERSION);
    }

    public int getLightningSize() {
        return entityData.get(LIGHTNING_DAMAGE_SIZE);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(LIGHTNING_COLOR, 0);
        entityData.define(PENETRATION, 0.0f);
        entityData.define(CRITICAL_CHANCE, 0.0f);
        entityData.define(LIGHTNING_CHANCE, 0.0f);
        entityData.define(CRIT_DAMAGE_MULT, 0.0f);
        entityData.define(LIGHTNING_DAMAGE_CONVERSION, 0.0f);
        entityData.define(LIGHTNING_DAMAGE_SIZE, 0);
        entityData.define(PROJ_ANTI_IMMUNE, false);
        entityData.define(FIRE_TICKS, 0);
        entityData.define(TARGET_ENTITY, 0);
        entityData.define(SPEED_MULT, 1.0f);
        entityData.define(TECH_LEVEL, (byte) TechLevel.WYVERN.index);
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
