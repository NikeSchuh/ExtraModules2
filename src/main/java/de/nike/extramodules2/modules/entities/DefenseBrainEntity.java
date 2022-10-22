package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.api.render.GuiHelper;
import com.brandon3055.brandonscore.client.BCSprites;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.nike.extramodules2.modules.ModuleTypes;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.data.DefenseSystemData;
import de.nike.extramodules2.utils.vectors.Vector2Float;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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

    @OnlyIn(Dist.CLIENT)
    private static Vector2Float currentPosition;

    public static final int EYE_COLOR = new Color(174, 167, 164).getRGB();

    private BooleanProperty activated;
    private boolean charged = false;

    public DefenseBrainEntity(Module<DefenseBrainData> module) {
        super(module);
        addProperty(activated = new BooleanProperty("defense_brain_module.activated", true).setFormatter(ConfigProperty.BooleanFormatter.ACTIVE_INACTIVE));
        this.savePropertiesToItem = true;
    }

    @Override
    public void tick(ModuleContext context) {
        if(charged) return;
        DefenseSystemData defenseSystemData = getDefenseSystemData();
        if(defenseSystemData.getOpCost() <= 0) return;
        IOPStorageModifiable storage = context.getOpStorage();
        if (!(context instanceof StackModuleContext && EffectiveSide.get().isServer() && storage != null)) {
            return;
        }
        StackModuleContext stackModuleContext = (StackModuleContext) context;
        if (!stackModuleContext.isEquipped()) {
            return;
        }

        if (storage.getOPStored() >= defenseSystemData.getOpCost()) {
            storage.modifyEnergyStored(-defenseSystemData.getOpCost());
            charged = true;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        if(!activated.getValue()) return;
        IVertexBuilder builder = getter.getBuffer(BCSprites.GUI_TYPE);
        MatrixStack matrixStack = new MatrixStack();

        //bounds
        int rectX = x + 12;
        int rectY = y + 14;
        int rectWidth = 8;
        int rectHeight = 6;

        //eye
        int eyeWidth = 4;
        int eyeHeight = 4;

        //GuiHelper.drawRect(getter, matrixStack, rectX, rectY, rectWidth, rectHeight, Color.WHITE.getRGB());


        int targetX = (int) Math.max(Math.min(mouseX - eyeWidth / 2, rectX + rectWidth - eyeWidth), rectX);
        int targetY = (int) Math.max(Math.min(mouseY - eyeHeight / 2, rectY + rectHeight - eyeHeight), rectY);

        if(currentPosition == null) {
            currentPosition = new Vector2Float(targetX, targetY);
        }

        Vector2Float next = Vector2Float.lerp(currentPosition, new Vector2Float(targetX, targetY), 0.05f);
        currentPosition = next;

        GuiHelper.drawRect(getter, new MatrixStack(), currentPosition.x , currentPosition.y, eyeWidth, eyeHeight, EYE_COLOR);
       // GuiHelperOld.drawSprite(builder, drawX, drawY, eyeWidth, eyeHeight, BCSprites.get(ExtraModules2.MODID, "textures/module/brain_eye.png").sprite(), 0);
    }

    public void attacked(LivingAttackEvent event) {
        if(!activated.getValue()) return;
        if(!charged) return;
        if(event.getSource().getEntity() == null) return;
        if(event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            if (event.getSource().getEntity() instanceof LivingEntity) {
                DefenseSystemData defenseSystemData = getDefenseSystemData();
                LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

                if(!player.canAttackType(attacker.getType())) return;
                if(!player.canAttack(attacker)) return;

                double mul = Math.max(defenseSystemData.getReflectedDamage() / 100, 0.85d);

                float damage = defenseSystemData.getReflectedDamage();
                if(defenseSystemData.isOdinsRage()) damage *= 1.25;

                Vector3d vector3f = attacker.position().subtract(player.position()).normalize().multiply(mul, mul, mul);
                attacker.setDeltaMovement(vector3f);
                attacker.hurt(DamageSource.indirectMagic(player, attacker), damage);
                player.level.playSound(null, attacker.blockPosition(), SoundEvents.ELDER_GUARDIAN_HURT, SoundCategory.HOSTILE, 0.7F, 0.8F);
                if(getDefenseSystemData().isOdinsRage()) {
                    EntityType.LIGHTNING_BOLT.spawn((ServerWorld) player.level, null, null, attacker.blockPosition(), SpawnReason.TRIGGERED, false, false);
                }
                charged = false;
            }
        }
    }

    public void creeperExplode(CreeperEntity creeper, ServerPlayerEntity player, ExplosionEvent.Start event) {
        if(!charged) return;
        event.setCanceled(true);
        DefenseSystemData defenseSystemData = getDefenseSystemData();
        double mul = Math.max(defenseSystemData.getReflectedDamage() / 100, 0.85d);
        float damage = defenseSystemData.getReflectedDamage();
        if(defenseSystemData.isOdinsRage()) damage *= 1.25;
        creeper.hurt(DamageSource.indirectMagic(player, creeper), damage);
        player.level.playSound(null, creeper.blockPosition(), SoundEvents.ELDER_GUARDIAN_HURT, SoundCategory.HOSTILE, 0.7F, 0.8F);
        if(getDefenseSystemData().isOdinsRage()) {
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
        if(context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            if(moduleContext.getEntity() instanceof ClientPlayerEntity) {
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
        if(context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            if(moduleContext.getEntity() instanceof ClientPlayerEntity) {
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
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if(stack.hasTag()) {
            charged = stack.getOrCreateTag().getBoolean("charged");
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putBoolean("charged", charged);
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        charged = compound.getBoolean("charged");
    }
}

