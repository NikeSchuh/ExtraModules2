package de.nike.extramodules2.items;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.ProjectileData;
import com.brandon3055.draconicevolution.api.modules.data.SpeedData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.entity.projectiles.DraconicBulletEntity;
import de.nike.extramodules2.modules.EMModuleCategories;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.PistolData;
import de.nike.extramodules2.modules.data.PistolLightningData;
import de.nike.extramodules2.modules.entities.PistolLightningEntity;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.ProjectileUtil;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ModularPistol extends ModularEnergyItem {

    private static final String TAG_SHOOT_COOLDOWN = "shootCooldown";

    private static final DecimalFormat numberFormat = new DecimalFormat("#.#");
    private final int gridWidth, gridHeight;
    private TechLevel level;
    private final float defaultDamage;

    public ModularPistol(TechPropBuilder builder, int gridWidth, int gridHeight, float baseDamage) {
        super(builder);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.level = builder.techLevel;
        this.defaultDamage = baseDamage;
    }

    @Override
    public ModuleHostImpl createHost(ItemStack stack) {
        ModuleHostImpl host = new ModuleHostImpl(tier, gridWidth, gridHeight, "pistol", false);
        host.addCategories(ModuleCategory.ENERGY, ModuleCategory.RANGED_WEAPON, EMModuleCategories.PISTOL);
        return host;
    }


    public static int getShootCooldown(ItemStack stack) {
        return NBTHelper.getInt(stack, TAG_SHOOT_COOLDOWN, 0);
    }
    public static void setShootCooldown(ItemStack stack, int value) {
        NBTHelper.setInt(stack, TAG_SHOOT_COOLDOWN, value);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int partialTicks, boolean bool) {
        if(getShootCooldown(stack) > 0) {
            setShootCooldown(stack, getShootCooldown(stack) -1);
        }
        super.inventoryTick(stack, world, entity, partialTicks, bool);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }


    @Override
    public void onUseTick(World p_219972_1_, LivingEntity p_219972_2_, ItemStack p_219972_3_, int p_219972_4_) {

    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(getShootCooldown(stack) <= 0) {
            LazyOptional<ModuleHost> optionalHost = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
            optionalHost.ifPresent(host -> {
                if(!player.level.isClientSide) {
                    LazyOptional<IOPStorage> optionalOpStorage = stack.getCapability(DECapabilities.OP_STORAGE);
                    optionalOpStorage.ifPresent(iopStorage -> {
                        IOPStorageModifiable modifiable = (IOPStorageModifiable) iopStorage;

                        ProjectileData projectileData = host.getModuleData(ModuleTypes.PROJ_MODIFIER, new ProjectileData(0, 0, 0, 0, 0));
                        SpeedData speedData = host.getModuleData(ModuleTypes.SPEED, new SpeedData(0));
                        PistolData pistolData = host.getModuleData(EMModuleTypes.PISTOL, new PistolData(0f, 0, 0, 0));
                        PistolLightningData pistolLightningData = host.getModuleData(EMModuleTypes.PISTOL_LIGHTNING, new PistolLightningData(0, 0.0f, 0.0f, 0, 0));

                        boolean lightningCharged = false;

                        PistolLightningEntity lightningEntity = (PistolLightningEntity) host.getEntitiesByType(EMModuleTypes.PISTOL_LIGHTNING).findFirst().orElse(null);

                        if(lightningEntity != null) lightningCharged = lightningEntity.isCharged();

                        boolean ignoreCancellation = host.getModuleData(ModuleTypes.PROJ_ANTI_IMMUNE) != null;

                        float velocity = (float) (((0.5F + (techLevelToBaseVel(getTechLevel()) / 4))) + (0.5 * projectileData.getVelocity()));
                        float baseDamage = calculateDamage(defaultDamage, pistolData.getExtraBaseDamage(), projectileData.getDamage(), projectileData.getVelocity());
                        int opCost = calculateCostPerShot(baseDamage, pistolData.getCriticalChance(), pistolData.getCriticalDamage(), pistolLightningData.getChance(), velocity);

                        if(modifiable.getEnergyStored() >= opCost) {
                            player.startUsingItem(hand);
                            int cooldown = (int) ((100) - (30 * speedData.getSpeedMultiplier()));
                            player.getCooldowns().addCooldown(stack.getItem(), cooldown);
                            setShootCooldown(stack, cooldown);
                            ServerWorld serverWorld = (ServerWorld) world;
                            serverWorld.playSound(null, player.blockPosition(), EMSounds.modularPistolShoot, SoundCategory.PLAYERS, 0.35f, 1.0f + (player.level.random.nextFloat() / 2));
                            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                            DraconicBulletEntity draconicBulletEntity = new DraconicBulletEntity(serverWorld, serverPlayerEntity);
                            draconicBulletEntity.setBaseDamage(baseDamage);
                            draconicBulletEntity.setCriticalChance(pistolData.getCriticalChance());
                            draconicBulletEntity.setCritDamageMult(2 + pistolData.getCriticalDamage());
                            draconicBulletEntity.setPenetration(projectileData.getPenetration());
                            draconicBulletEntity.setTechLevel(getTechLevel());

                            draconicBulletEntity.setLightningColor(getProjAndLightningColor(level));
                            draconicBulletEntity.setLightningChance(lightningCharged ? pistolLightningData.getChance() : 0.0F);
                            draconicBulletEntity.setLightningDamageConversion(pistolLightningData.getSurroundingDamage());
                            draconicBulletEntity.setLightningDamageSize(pistolLightningData.getRange());
                            draconicBulletEntity.setPistolLightningEntity(lightningEntity);

                            draconicBulletEntity.setFireTicks(pistolData.getFireTicks());
                            draconicBulletEntity.setOwner(serverPlayerEntity);
                            draconicBulletEntity.setIgnoreCancellation(ignoreCancellation);
                            draconicBulletEntity.shootFromRotation(player, player.xRot, player.yRot, 1.0F, Math.min(velocity, 4.0f), 0.1f + (-1 * projectileData.getAccuracy()));

                            if(host.getModuleData(EMModuleTypes.PISTOL_HOMING) != null) {
                                EntityRayTraceResult result = ProjectileUtil.getFirstEntityHit(draconicBulletEntity.level, draconicBulletEntity, draconicBulletEntity.position(), player.getLookAngle().normalize(), 100, new AxisAlignedBB(1, 1, 1, -1, -1, -1), new Predicate<Entity>() {
                                    @Override
                                    public boolean test(Entity entity) {
                                        return !(entity.equals(player)) && !(entity instanceof DraconicBulletEntity) && entity.isAlive() && entity.isAttackable();
                                    }
                                });
                                if(result != null) draconicBulletEntity.setTarget(result.getEntity());
                                draconicBulletEntity.setSpeedMult(projectileData.getVelocity() + 1);
                            }
                            serverWorld.addFreshEntity(draconicBulletEntity);
                            modifiable.modifyEnergyStored(-opCost);
                        }
                    });
                }
            });
        }
        return super.use(world, player, hand);
    }

    public static float calculateDamage(float pistolDefaultDamage, float pistolExtraBaseDamage, float damageMultiplier, float velocityMultiplier) {
        float baseDamage = pistolDefaultDamage + pistolExtraBaseDamage;
        baseDamage += (baseDamage * (velocityMultiplier / 6));
        return baseDamage + (baseDamage * damageMultiplier);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        LazyOptional<ModuleHost> optionalHost = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
        optionalHost.ifPresent(host -> {

            ProjectileData projectileData = host.getModuleData(ModuleTypes.PROJ_MODIFIER, new ProjectileData(0, 0, 0, 0, 0));
            SpeedData speedData = host.getModuleData(ModuleTypes.SPEED, new SpeedData(0));
            PistolData pistolData = host.getModuleData(EMModuleTypes.PISTOL, new PistolData(0f, 0, 0, 0));
            PistolLightningData pistolLightningData = host.getModuleData(EMModuleTypes.PISTOL_LIGHTNING, new PistolLightningData(0, 0.0f, 0.0f, 0, 0));

            float velocity = (float) (((0.5F + (techLevelToBaseVel(getTechLevel()) / 4))) + (0.5 * projectileData.getVelocity()));
            float baseDamage = calculateDamage(defaultDamage, pistolData.getExtraBaseDamage(), projectileData.getDamage(), projectileData.getVelocity());
            float maximumPossible = baseDamage * (2 + pistolData.getCriticalDamage());
            int opCost = calculateCostPerShot(baseDamage, pistolData.getCriticalChance(), pistolData.getCriticalDamage(), pistolLightningData.getChance(), velocity);

            if(pistolData.getCriticalChance() >= 1.0F) baseDamage = maximumPossible;
            if(pistolData.getCriticalChance() <= 0.0F) maximumPossible = baseDamage;

            tooltip.add(TranslationUtils.string(TextFormatting.DARK_GREEN +""+ numberFormat.format(baseDamage) + " Damage"));
            tooltip.add(TranslationUtils.string(TextFormatting.DARK_GREEN +""+ FormatUtils.formatE(opCost) + " OP/shot"));
            if(Screen.hasShiftDown()) {
                tooltip.add(TranslationUtils.string(TextFormatting.DARK_GREEN +""+ numberFormat.format(maximumPossible) + " Max Damage"));
            }
        });
    }



    public static int calculateCostPerShot(double damage, float criticalChance, float criticalDamage, float lightningChance, float velocity) {
        return Math.min(2000000000,(int) (((Math.pow(damage, 2.8) - Math.pow(damage, 2.5)) * (1 + ((criticalChance * criticalDamage) / 2))) * (1 + (velocity / 8)) + (Math.pow(damage, 2) * (lightningChance * 20))));
    }

    public static int techLevelToBaseVel(TechLevel level) {
        switch (level) {
            case WYVERN:
                return 1;
            case DRACONIC:
                return 2;
            case CHAOTIC:
                return 3;
            default:
                return 0;
        }
    }

    public static int getProjAndLightningColor(TechLevel level) {
        switch (level) {
            case WYVERN:
                return Color.magenta.getRGB();
            case DRACONIC:
                return 0xff8c00;
            case CHAOTIC:
                return Color.red.getRGB();
            default:
                return Color.WHITE.getRGB();
        }
    }
}
