package de.nike.extramodules2.items;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.math.Vector2;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.api.power.OPStorage;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.ProjectileData;
import com.brandon3055.draconicevolution.api.modules.data.SpeedData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEModules;
import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.entity.EMEntities;
import de.nike.extramodules2.entity.projectiles.DraconicBulletEntity;
import de.nike.extramodules2.modules.EMModuleCategories;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.PistolData;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class ModularPistol extends ModularEnergyItem {

    private static final String TAG_SHOOT_COOLDOWN = "shootCooldown";

    private final int gridWidth, gridHeight;
    private TechLevel level;

    public ModularPistol(TechPropBuilder builder, int gridWidth, int gridHeight) {
        super(builder);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.level = builder.techLevel;
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
                        PistolData pistolData = host.getModuleData(EMModuleTypes.PISTOL, new PistolData(0f));

                        float velocity = (float) (((0.5F + (techLevelToBaseVel(getTechLevel()) / 4))) + (0.5 * projectileData.getVelocity()));
                        double baseDamage = 10 + (10 * projectileData.getDamage());

                        int opCost = calculateCostPerShot(baseDamage, pistolData.getCriticalChance(), 0.5f, velocity);

                        if(modifiable.getEnergyStored() >= opCost) {
                            player.level.playSound(null, player.position().x, player.position().y, player.position().z, EMSounds.modularPistolShoot, SoundCategory.PLAYERS, 1.0f, 1.0f + (player.level.random.nextFloat() / 2));
                            int cooldown = (int) ((100) - (30 * speedData.getSpeedMultiplier()));
                            player.getCooldowns().addCooldown(stack.getItem(), cooldown);
                            setShootCooldown(stack, cooldown);
                            ServerWorld serverWorld = (ServerWorld) world;
                            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                            DraconicBulletEntity draconicBulletEntity = new DraconicBulletEntity(serverWorld, serverPlayerEntity);
                            draconicBulletEntity.setLightningColor(getProjAndLightningColor(level));
                            draconicBulletEntity.setBaseDamage(baseDamage);
                            draconicBulletEntity.setCriticalChance(pistolData.getCriticalChance());
                            draconicBulletEntity.setPenetration(projectileData.getPenetration());
                            draconicBulletEntity.setOwner(serverPlayerEntity);
                            draconicBulletEntity.shootFromRotation(player, player.xRot, player.yRot, 1.0F, Math.min(velocity, 4.0f), 0.1f + (-1 * projectileData.getAccuracy()));
                            serverWorld.addFreshEntity(draconicBulletEntity);
                            modifiable.modifyEnergyStored(-opCost);
                        }
                    });
                }
            });
        }
        return super.use(world, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        LazyOptional<ModuleHost> optionalHost = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
        optionalHost.ifPresent(host -> {
            ProjectileData projectileData = host.getModuleData(ModuleTypes.PROJ_MODIFIER, new ProjectileData(0, 0, 0, 0, 0));
            SpeedData speedData = host.getModuleData(ModuleTypes.SPEED, new SpeedData(0));

            float velocity = (float) (((0.5F + (techLevelToBaseVel(getTechLevel()) / 4))) + (0.5 * projectileData.getVelocity()));
            double baseDamage = 10 + (10 * projectileData.getDamage());

            int opCost = calculateCostPerShot(baseDamage, 0.0f, 0.0f, velocity);
            tooltip.add(TranslationUtils.string(TextFormatting.DARK_GREEN +""+ FormatUtils.formatE(baseDamage) + " damage"));
            tooltip.add(TranslationUtils.string(TextFormatting.DARK_GREEN +""+ FormatUtils.formatE(opCost) + "/OP per shot"));
        });
    }





    public static int calculateCostPerShot(double damage, float criticalChance, float lightningChance, float velocity) {
        return (int) Math.round(Math.pow(damage, 3) * velocity + ((damage * 4) * criticalChance) + ((damage * 2) * lightningChance * 2));
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
                return 0xff4f00;
            case CHAOTIC:
                return Color.red.getRGB();
            default:
                return Color.WHITE.getRGB();
        }
    }
}
