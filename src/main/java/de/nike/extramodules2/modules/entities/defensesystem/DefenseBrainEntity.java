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
import de.nike.extramodules2.modules.ModuleTypes;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.data.DefenseSystemData;
import de.nike.extramodules2.network.EMNetwork;
import de.nike.extramodules2.utils.vectors.Vector2Float;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.awt.*;

public class DefenseBrainEntity extends ModuleEntity {

    public static final int EYE_COLOR = new Color(174, 167, 164).getRGB();
    public static final int RAGE_COLOR = new Color(255, 0, 0, 25).getRGB();
    // Client Stuff
    @OnlyIn(Dist.CLIENT)

    private static Vector2Float currentPosition;
    private static Vector2Float currentTarget;
    private static EyeMode gurdianEyeMode = EyeMode.CHASING;
    private static int lastRectX = 0;
    private static int lastRectY = 0;
    private static int targetChangeDelay = 0;
    // Rage Mode
    private final int EYE_POSITION_CHANGE_DELAY = 5;
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
    private int rageModeTicks = 0;

    public DefenseBrainEntity(Module<DefenseBrainData> module) {
        super(module);
        addProperty(activated = new BooleanProperty("defense_brain_module.activated", true).setFormatter(ConfigProperty.BooleanFormatter.ACTIVE_INACTIVE));
        this.savePropertiesToItem = true;
    }

    @OnlyIn(Dist.CLIENT)
    public static void modeChange(int mode) {
        System.out.println("Raw Int: " + mode);
        // System.out.println("Server -> Client Mode Change: from " +
        // gurdianEyeMode.name() + " to " + EyeMode.valueOf(mode));
        gurdianEyeMode = EyeMode.valueOf(mode);
    }

    @Override
    public void tick(ModuleContext context) {
        StackModuleContext stackModuleContext = (StackModuleContext) context;
        if (!stackModuleContext.isEquipped()) {
            return;
        }

        // Client Stuff
        if (EffectiveSide.get().isClient()) {
            if (stackModuleContext.getEntity() instanceof ClientPlayerEntity) {
                ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity) stackModuleContext.getEntity();
                ClientWorld clientWorld = clientPlayerEntity.clientLevel;

                if (gurdianEyeMode == EyeMode.RAGE) {
                    if (targetChangeDelay > 0) targetChangeDelay--;
                    if (targetChangeDelay <= 0) {
                        setEyeTarget(clientWorld.random.nextInt(10000) - 5000, clientWorld.random.nextInt(10000) - 5000, lastRectX, lastRectY);
                        targetChangeDelay = EYE_POSITION_CHANGE_DELAY;
                    }
                }

                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(clientPlayerEntity.position().add(10, 5, 10), clientPlayerEntity.position().subtract(10, 5, 10));
                for (MonsterEntity monsterEntity : clientWorld.getEntitiesOfClass(MonsterEntity.class, axisAlignedBB)) {
                    Vector3d monsterPosition = monsterEntity.position();
                    for (int i = 0; i < 2; i++) {
                        clientWorld.addParticle(ParticleTypes.SOUL, monsterPosition.x, monsterPosition.y + (monsterEntity.getEyeHeight() * Math.random()), monsterPosition.z, 0.1 * Math.random(), 0.1 * Math.random(), 0.1 * Math.random());
                    }
                }
            }
        }

        // Server stuff

        if (stackModuleContext.getEntity() instanceof ServerPlayerEntity) {
            if (rageModeTicks > 0) {
                rageModeTicks--;
            }
            if (rageModeTicks == 0) {
                EMNetwork.sendEyeChangeMode((ServerPlayerEntity) stackModuleContext.getEntity(), EyeMode.CHASING.getValue());
                rageModeTicks = -1;
            }

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
        }

        if (currentPosition == null) {
            currentPosition = new Vector2Float(rectX, rectY);
        }
        if (gurdianEyeMode == EyeMode.CHASING) {
            setEyeTarget(mouseX, mouseY, rectX, rectY);
        }

        Vector2Float next = Vector2Float.lerp(currentPosition, currentTarget, 0.05f);
        currentPosition = next;

        GuiHelper.drawRect(getter, new MatrixStack(), currentPosition.x, currentPosition.y, eyeWidth, eyeHeight, EYE_COLOR);
        // GuiHelperOld.drawSprite(builder, drawX, drawY, eyeWidth, eyeHeight,
        // BCSprites.get(ExtraModules2.MODID, "textures/module/brain_eye.png").sprite(),
        // 0);
    }

    @OnlyIn(Dist.CLIENT)
    public void setEyeTarget(double x, double y, int rectX, int rectY) {
        float targetX = (float) Math.max(Math.min(x - eyeWidth / 2, rectX + rectWidth - eyeWidth), rectX);
        float targetY = (float) Math.max(Math.min(y - eyeHeight / 2, rectY + rectHeight - eyeHeight), rectY);
        currentTarget = new Vector2Float(targetX, targetY);
    }

    public void attacked(LivingAttackEvent event) {
        if (!activated.getValue()) return;
        if (!charged) return;
        if (event.getSource().getEntity() == null) return;
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            if (event.getSource().getEntity() instanceof LivingEntity) {
                DefenseSystemData defenseSystemData = getDefenseSystemData();
                LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

                rageModeTicks = 100;
                EMNetwork.sendEyeChangeMode(player, EyeMode.RAGE.getValue());

                if (!player.canAttackType(attacker.getType())) return;
                if (!player.canAttack(attacker)) return;

                double mul = Math.max(defenseSystemData.getReflectedDamage() / 100, 0.85d);

                float damage = defenseSystemData.getReflectedDamage();
                if (defenseSystemData.isOdinsRage()) damage *= 1.25;

                Vector3d vector3f = attacker.position().subtract(player.position()).normalize().multiply(mul, mul, mul);
                attacker.setDeltaMovement(vector3f);
                attacker.hurt(DamageSource.indirectMagic(player, attacker), damage);
                player.level.playSound(null, attacker.blockPosition(), SoundEvents.ELDER_GUARDIAN_HURT, SoundCategory.HOSTILE, 0.7F, 0.8F);
                if (getDefenseSystemData().isOdinsRage()) {
                    EntityType.LIGHTNING_BOLT.spawn((ServerWorld) player.level, null, null, attacker.blockPosition(), SpawnReason.TRIGGERED, false, false);
                }
                charged = false;
            }
        }
    }

    public void creeperExplode(CreeperEntity creeper, ServerPlayerEntity player, ExplosionEvent.Start event) {
        if (!charged) return;
        event.setCanceled(true);
        DefenseSystemData defenseSystemData = getDefenseSystemData();
        double mul = Math.max(defenseSystemData.getReflectedDamage() / 100, 0.85d);
        float damage = defenseSystemData.getReflectedDamage();
        if (defenseSystemData.isOdinsRage()) damage *= 1.25;
        creeper.hurt(DamageSource.indirectMagic(player, creeper), damage);
        player.level.playSound(null, creeper.blockPosition(), SoundEvents.ELDER_GUARDIAN_HURT, SoundCategory.HOSTILE, 0.7F, 0.8F);
        if (getDefenseSystemData().isOdinsRage()) {
            EntityType.LIGHTNING_BOLT.spawn((ServerWorld) player.level, null, null, creeper.blockPosition(), SpawnReason.TRIGGERED, false, false);
        }
        charged = false;
    }

    public void damaged(LivingDamageEvent event) {
        return;
    }

    public DefenseSystemData getDefenseSystemData() {
        return host.getModuleData(ModuleTypes.DEFENSE_SYSTEM, new DefenseSystemData(0, 0, false));
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
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if (stack.hasTag()) {
            charged = stack.getOrCreateTag().getBoolean("charged");
            rageModeTicks = stack.getOrCreateTag().getInt("ragemodeticks");
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putBoolean("charged", charged);
        compound.putInt("ragemodeticks", rageModeTicks);
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        charged = compound.getBoolean("charged");
        rageModeTicks = compound.getInt("ragemodeticks");
    }
}
