package de.nike.extramodules2.modules.entities.defensesystem;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.api.render.GuiHelper;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.nike.extramodules2.damagesources.DamageSourceDefenseSystem;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.data.DefenseSystemData;
import de.nike.extramodules2.network.EMNetwork;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.NikesMath;
import de.nike.extramodules2.utils.TranslationUtils;
import de.nike.extramodules2.utils.vectors.Vector2Float;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefenseBrainEntity extends ModuleEntity {

    public static final int EYE_COLOR = new Color(174, 167, 164).getRGB();
    public static final int RAGE_COLOR = new Color(255, 0, 0, 25).getRGB();
    public static final int RAGE_COLOR_EYE = new Color(255, 0, 0, 50).getRGB();


    // Client Stuff

    private static Vector2Float currentPosition;
    private static Vector2Float currentTarget;
    private static EyeMode gurdianEyeMode = EyeMode.CHASING;
    private static int lastRectX = 0;
    private static int lastRectY = 0;
    private static int targetChangeDelay = 0;
    private static float rageModeChargeClient = 0.0f;

    private static float lastProgress = 0.0f;
    // Rage Mode
    private final int EYE_POSITION_CHANGE_DELAY = 5;
    private final float RAGE_CHARGE_LOSE = 0.005f;
    // Eye Rect
    private final int rectWidth = 8;
    // UNSAFE!
    private final int rectHeight = 6;
    private final int rectOffsetX = 12;
    // UNSAFE!
    private final int rectOffsetY = 14;
    // Eye Size
    private final int eyeWidth = 4;


    // Server stuff;
    private final int eyeHeight = 4;
    private BooleanProperty activated;
    private boolean charged = false;
    private float rageModeChargeServer = 0;
    private int rageModeTicks = 0;
    private double damageDelt = 0;

    public DefenseBrainEntity(Module<DefenseBrainData> module) {
        super(module);
        addProperty(activated = new BooleanProperty("defense_brain_module.activated", true).setFormatter(ConfigProperty.BooleanFormatter.ACTIVE_INACTIVE));
        this.savePropertiesToItem = true;
    }

    @OnlyIn(Dist.CLIENT)
    public static void modeChange(int mode) {
        gurdianEyeMode = EyeMode.valueOf(mode);
        if (mode == EyeMode.CHASING.getValue()) rageModeChargeClient = 0.01f;
    }

    @OnlyIn(Dist.CLIENT)
    public static void rageModeCharge(float charge) {
        rageModeChargeClient = charge;
    }

    @Override
    public void tick(ModuleContext context) {
        if (!(context instanceof StackModuleContext)) return;
        StackModuleContext stackModuleContext = (StackModuleContext) context;
        if (!stackModuleContext.isEquipped()) return;
        if (!(stackModuleContext.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity playerEntity = (PlayerEntity) stackModuleContext.getEntity();
        tickRageModeCharge(stackModuleContext, (PlayerEntity) stackModuleContext.getEntity());
        if (EffectiveSide.get().isClient()) {
            ClientPlayerEntity clientPlayerEntity = ((ClientPlayerEntity) stackModuleContext.getEntity());
            ClientWorld clientWorld = clientPlayerEntity.clientLevel;
            if (isInRageMode()) {
                eyeVisualsRageMode(clientWorld);
            }
            // Random Messages
            if(playerEntity.tickCount % 10000 == 0) {
                if(Math.random() < 0.1D) {
                    playerEntity.sendMessage(new TranslationTextComponent("module.extramodules2.defense_brain.error"), null);
                    int msg = playerEntity.level.random.nextInt(5);
                    playerEntity.sendMessage(new TranslationTextComponent("module.extramodules2.defense_brain.randomtick" + msg), null);
                    playerEntity.playSound(SoundEvents.GUARDIAN_AMBIENT, 0.8f, 2f);
                }
            }
        } else {
            // Server stuff
            if (stackModuleContext.getEntity() instanceof ServerPlayerEntity) {
                if (charged) return;
                DefenseSystemData defenseSystemData = getDefenseSystemData();
                if (defenseSystemData.getOpCost() <= 0) return;
                IOPStorageModifiable storage = context.getOpStorage();
                if (!(context instanceof StackModuleContext && EffectiveSide.get().isServer() && storage != null)) {
                    return;
                }
                if (storage.getOPStored() >= defenseSystemData.getOpCost()) {
                    storage.modifyEnergyStored(-defenseSystemData.getOpCost());
                    charged = true;
                }
            }
        }
    }

    public void attacked(LivingAttackEvent event) {
        if (!activated.getValue()) return;
        if (!charged) return;
        if (event.getSource().getEntity() == null) return;
        if(event.getSource().getEntity() instanceof AreaEffectCloudEntity) return;
        if(!(event.getEntityLiving() instanceof ServerPlayerEntity)) return;
        if(!(event.getSource().getEntity() instanceof LivingEntity)) return;
        if(event.getSource().isMagic()) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
        DefenseSystemData defenseSystemData = getDefenseSystemData();
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        rageChargeLogicAttack(player, event.getAmount());

        // Knockback/Damage
        double mul = Math.max(defenseSystemData.getReflectedDamage() / 100, 0.5d);
        float damage = defenseSystemData.getReflectedDamage();
        if (defenseSystemData.isOdinsRage()) damage *= 1.25;
        damageDelt+=damage;
        Vector3d vector3f = attacker.position().subtract(player.position()).normalize().multiply(mul, mul, mul);
        attacker.setDeltaMovement(vector3f);
        DamageSource damageSource = new DamageSourceDefenseSystem(player);
        attacker.hurt(damageSource, damage);
        player.level.playSound(null, attacker.blockPosition(), SoundEvents.ELDER_GUARDIAN_HURT, SoundCategory.HOSTILE, 0.7F, 0.8F);
        if (getDefenseSystemData().isOdinsRage()) {
            EntityType.LIGHTNING_BOLT.spawn((ServerWorld) player.level, null, null, attacker.blockPosition(), SpawnReason.TRIGGERED, false, false);
        }
        charged = false;
    }

    public void rageChargeLogicAttack(ServerPlayerEntity player, float amount) {
        if (!(gurdianEyeMode == EyeMode.RAGE)) {
            if (amount > 0.2f) {
                rageModeChargeServer = Math.min(1.0f, rageModeChargeServer + (amount / 50));
                EMNetwork.sendEyeRageCharge(player, rageModeChargeServer);
                if (rageModeChargeServer >= 1.0f) {
                    serverActivateRageMode(player);
                }
            }
        } else rageModeTicks = 100;
    }

    public void eyeVisualsRageMode(ClientWorld clientWorld) {
        if (targetChangeDelay > 0) targetChangeDelay--;
        if (targetChangeDelay <= 0) {
            setEyeTarget(clientWorld.random.nextInt(10000) - 5000, clientWorld.random.nextInt(10000) - 5000, lastRectX, lastRectY);
            targetChangeDelay = EYE_POSITION_CHANGE_DELAY;
        }
    }

    public void serverDeactivateRageMode(ServerPlayerEntity serverPlayerEntity) {
        EMNetwork.sendEyeChangeMode(serverPlayerEntity, EyeMode.CHASING.getValue());
        rageModeChargeServer = 0;
        rageModeTicks = -1;
        gurdianEyeMode = EyeMode.CHASING;
    }

    public void clientActivateRageMode(ClientPlayerEntity clientPlayerEntity) {

    }

    public void setRageModeChargeServer(float rageModeChargeServer) {
        this.rageModeChargeServer = rageModeChargeServer;
    }

    public boolean isInRageMode() {
        return gurdianEyeMode == EyeMode.RAGE;
    }

    public void serverActivateRageMode(ServerPlayerEntity player) {
        DefenseBrainData data = (DefenseBrainData) module.getData();
        gurdianEyeMode = EyeMode.RAGE;
        rageModeChargeServer = 0;
        rageModeTicks = data.getInitialRageTicks();

        player.level.playSound(null, player.blockPosition(), SoundEvents.GUARDIAN_DEATH, SoundCategory.PLAYERS, 3f, 0.5f);
        player.level.playSound(null, player.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 1f, (float) (Math.random() + Math.random()));
        EMNetwork.sendEyeChangeMode(player, EyeMode.RAGE.getValue());

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(player.position().add(5, 3, 5), player.position().subtract(5, 3, 5));
        for (LivingEntity livingEntity : player.level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB)) {
            if (livingEntity.equals(player)) continue;
            Vector3d livingPosition = livingEntity.position();
            Vector3d throwDirection = livingPosition.subtract(player.position()).normalize().multiply(2.5f, 2.5f, 2.5f);
            livingEntity.setDeltaMovement(throwDirection);
            livingEntity.hurt(DamageSource.CACTUS, 10);
        }

    }

    private static float currentPitch = 0.5f;
    private static boolean rising = true;

    public void tickRageModeCharge(StackModuleContext stackModuleContext, PlayerEntity playerEntity) {
        if (EffectiveSide.get().isServer()) {
            if (stackModuleContext.getOpStorage() != null) {
                IOPStorageModifiable storage = stackModuleContext.getOpStorage();
                if (isInRageMode()) {
                    DefenseBrainData data = (DefenseBrainData) module.getData();
                    rageModeTicks--;
                    if (storage.getEnergyStored() > data.getRageModeTickCost()) {
                        storage.modifyEnergyStored(-data.getRageModeTickCost());
                    } else {
                        rageModeTicks = Math.max(0, rageModeTicks - 20);
                    }
                    // Damage
                    rageModeDamage(playerEntity,data, storage);
                }
            }
            if (rageModeChargeServer > 0.0f)
                rageModeChargeServer = Math.max(0, rageModeChargeServer - RAGE_CHARGE_LOSE);
            if (rageModeTicks == 0) {
                serverDeactivateRageMode((ServerPlayerEntity) playerEntity);
            }
        } else {
            if (gurdianEyeMode != EyeMode.RAGE && rageModeChargeClient > 0) {
                rageModeChargeClient = Math.max(0, rageModeChargeClient - RAGE_CHARGE_LOSE);
            }
            if (gurdianEyeMode == EyeMode.RAGE) {
                if(playerEntity.tickCount % 3 == 0) {
                    playerEntity.playSound(SoundEvents.GUARDIAN_AMBIENT, 0.5f, currentPitch);
                    playerEntity.playSound(SoundEvents.ELDER_GUARDIAN_AMBIENT, 0.5f, currentPitch);
                    if (rising) {
                        currentPitch += 0.2f;
                    } else currentPitch -= 0.2f;

                    if (currentPitch > 2) {
                        rising = false;
                    } else if (currentPitch < 0.5) rising = true;
                }
            }
        }
    }

   private static DecimalFormat time = new DecimalFormat("#.###");

    public void rageModeDamage(PlayerEntity playerEntity,DefenseBrainData data, IOPStorageModifiable storage){
        if(playerEntity.tickCount % data.getShootCooldown() == 0) {
            double range = data.getRageModeRange();
            boolean b = false;
            DefenseSystemData systemData = getDefenseSystemData();
            long nanos = System.nanoTime();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(playerEntity.position().add(range, range / 2, range), playerEntity.position().subtract(range, range / 2, range));
            List<LivingEntity> livingEntities = playerEntity.level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
            Vector3i playerPos = playerEntity.blockPosition();
            livingEntities.sort(Comparator.comparing(l -> l.blockPosition().distSqr(playerPos)));
            for (LivingEntity livingEntity : livingEntities) {
                if (livingEntity.equals(playerEntity)) continue;
                if (livingEntity.invulnerableTime > 0) continue;
                if (livingEntity.isInvulnerable()) continue;
                if (livingEntity.isDeadOrDying()) continue;
                if (livingEntity.getEntity() instanceof AreaEffectCloudEntity) continue;
                if (storage.getEnergyStored() < systemData.getOpCost()) continue;
                if (!playerEntity.canSee(livingEntity)) continue;
                if(!playerEntity.canAttack(livingEntity)) continue;
                storage.modifyEnergyStored(-systemData.getOpCost());
                livingEntity.hurt(new DamageSourceDefenseSystem(playerEntity), systemData.getReflectedDamage());
                damageDelt += systemData.getReflectedDamage();
                EMNetwork.sendEyeShootEffect(playerEntity.position().add(0, 1, 0), livingEntity.getEyePosition(livingEntity.getEyeHeight() - 0.1f), livingEntity.level.dimension(), playerEntity.blockPosition(), 20D);
                playerEntity.level.playSound(null, playerEntity.blockPosition(), SoundEvents.GUARDIAN_HURT, SoundCategory.PLAYERS, 10f, (float) (2f - Math.random()));
                if (getDefenseSystemData().isOdinsRage()) {
                    EntityType.LIGHTNING_BOLT.spawn((ServerWorld) livingEntity.level, null, null, livingEntity.blockPosition(), SpawnReason.TRIGGERED, true, true);
                }
                b = true;
                break;
            }
            if (b) rageModeTicks = data.getInitialRageTicks();

        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        if (!activated.getValue()) return;
        MatrixStack matrixStack = new MatrixStack();

        // Eye Bounds / Position
        int rectX = lastRectX = x + rectOffsetX;
        int rectY = lastRectY = y + rectOffsetY;

        // GuiHelper.drawRect(getter, matrixStack, rectX, rectY, rectWidth, rectHeight,
        // Color.WHITE.getRGB()); // BoundRect


        if (gurdianEyeMode == EyeMode.RAGE) {
            GuiHelper.drawRect(getter, matrixStack, x, y, width, height, RAGE_COLOR);
            GuiHelper.drawRect(getter, matrixStack, rectX - 2, rectY, rectWidth + 4, rectHeight, RAGE_COLOR_EYE);
        }

        if (currentPosition == null) {
            currentPosition = new Vector2Float(rectX, rectY);
        }
        if (gurdianEyeMode == EyeMode.CHASING) {
            setEyeTarget(mouseX, mouseY, rectX, rectY);
        }

        if ((rageModeChargeClient > 0 || lastProgress > 0) && !isInRageMode()) {
            lastProgress = NikesMath.lerp(lastProgress, rageModeChargeClient, 0.01f);
            GuiHelper.drawRect(getter, matrixStack, x, (y + height) - (height * lastProgress), width, height * lastProgress, RAGE_COLOR);
        }

        Vector2Float next = Vector2Float.lerp(currentPosition, currentTarget, 0.05f);
        currentPosition = next;
        GuiHelper.drawRect(getter, new MatrixStack(), currentPosition.x, currentPosition.y, eyeWidth, eyeHeight, EYE_COLOR);
    }

    @OnlyIn(Dist.CLIENT)
    public void setEyeTarget(double x, double y, int rectX, int rectY) {
        float targetX = (float) Math.max(Math.min(x - eyeWidth / 2, rectX + rectWidth - eyeWidth), rectX);
        float targetY = (float) Math.max(Math.min(y - eyeHeight / 2, rectY + rectHeight - eyeHeight), rectY);
        currentTarget = new Vector2Float(targetX, targetY);
    }

    public void creeperExplode(CreeperEntity creeper, ServerPlayerEntity player, ExplosionEvent.Start event) {
        if (!charged) return;
        DefenseSystemData defenseSystemData = getDefenseSystemData();
        double mul = Math.max(defenseSystemData.getReflectedDamage() / 100, 0.85d);
        float damage = defenseSystemData.getReflectedDamage();
        if (defenseSystemData.isOdinsRage()) damage *= 1.25;
        if(damage < creeper.getHealth()) return;
        event.setCanceled(true);
        creeper.hurt(DamageSource.indirectMagic(player, creeper), damage);
        damageDelt += damage;
        player.level.playSound(null, creeper.blockPosition(), SoundEvents.ELDER_GUARDIAN_HURT, SoundCategory.HOSTILE, 0.7F, 0.8F);
        if (getDefenseSystemData().isOdinsRage()) {
            EntityType.LIGHTNING_BOLT.spawn((ServerWorld) player.level, null, null, creeper.blockPosition(), SpawnReason.TRIGGERED, false, false);
        }
        charged = false;
    }

    public DefenseSystemData getDefenseSystemData() {
        return host.getModuleData(EMModuleTypes.DEFENSE_SYSTEM, new DefenseSystemData(0, 0, false));
    }

    public BooleanProperty getActivated() {
        return activated;
    }

    public float getRageModeChargeClient() {
        return rageModeChargeClient;
    }


    private static final DecimalFormat format = new DecimalFormat("#.##");

    @Override
    public void addToolTip(List<ITextComponent> list) {
        DefenseBrainData data = (DefenseBrainData) module.getData();
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.ragecost") + ": " + TextFormatting.GREEN + "" + FormatUtils.formatE(data.getRageModeTickCost()) + " OP/t"));
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.ragerange") + ": " + TextFormatting.GREEN + "" + ((int) data.getRageModeRange()) + "x" + ((int) data.getRageModeRange())));
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.rageticks") + ": " + TextFormatting.GREEN + "" + format.format(data.getInitialRageTicks() / 20D) + "s"));
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.shootdelay") + ": " + TextFormatting.GREEN + "" + format.format((data.getShootCooldown()) / 20D) + "s"));
        if(damageDelt > 0) {
            list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.damagedelt") + ": " + TextFormatting.GREEN + "" + FormatUtils.formatE(damageDelt) + ""));
        }
        if(!isInRageMode()) {
            list.add(new StringTextComponent(TextFormatting.GRAY + "Rage: " + TextFormatting.GREEN + Math.round(rageModeChargeClient * 100) + "%"));
        } else list.add(new StringTextComponent(TextFormatting.GRAY + "Rage: "+ TextFormatting.GREEN + "100%"));
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void onInstalled(ModuleContext context) {
        context.getType();
        if (context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            if (moduleContext.getEntity() instanceof ClientPlayerEntity) {
                PlayerEntity serverPlayerEntity = (PlayerEntity) moduleContext.getEntity();
                serverPlayerEntity.playSound(SoundEvents.ELDER_GUARDIAN_DEATH, 1f, 0.5f);
                serverPlayerEntity.sendMessage(new TranslationTextComponent("module.extramodules2.defense_brain.bootup"), null);
            }
        }
        super.onInstalled(context);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRemoved(ModuleContext context) {
        currentPosition = null;
        if (context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            if (moduleContext.getEntity() instanceof ClientPlayerEntity) {
                ClientPlayerEntity playerEntity = (ClientPlayerEntity) moduleContext.getEntity();
                playerEntity.playSound(SoundEvents.BEACON_DEACTIVATE, 1f, 0.8f);
                playerEntity.sendMessage(new TranslationTextComponent("module.extramodules2.defense_brain.shutdown"), null);
            }
        }
        super.onRemoved(context);
    }

    @Override
    public void writeToItemStack(ItemStack stack, ModuleContext context) {
        super.writeToItemStack(stack, context);
        stack.getOrCreateTag().putBoolean("charged", charged);
        stack.getOrCreateTag().putInt("ragemodeticks", rageModeTicks);
        stack.getOrCreateTag().putFloat("rage", rageModeChargeServer);
        stack.getOrCreateTag().putDouble("damagedelt", damageDelt);
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if (stack.hasTag()) {
            charged = stack.getOrCreateTag().getBoolean("charged");
            rageModeTicks = stack.getOrCreateTag().getInt("ragemodeticks");
            rageModeChargeServer = stack.getOrCreateTag().getFloat("rage");
            damageDelt = stack.getOrCreateTag().getDouble("damagedelt");
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putBoolean("charged", charged);
        compound.putInt("ragemodeticks", rageModeTicks);
        compound.putFloat("rage", rageModeChargeServer);
        compound.putDouble("damagedelt", damageDelt);
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        charged = compound.getBoolean("charged");
        rageModeTicks = compound.getInt("ragemodeticks");
        rageModeChargeServer = compound.getFloat("rage");
        damageDelt = compound.getDouble("damagedelt");
    }
}
