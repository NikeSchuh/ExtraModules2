package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.render.GuiHelper;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.HitCooldownData;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import de.nike.extramodules2.network.EMNetwork;
import de.nike.extramodules2.utils.NikesMath;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class HitCooldownEntitiy extends ModuleEntity {

    private static int invulnerableTicksClient = 0;

    private int invulnerableTicks = 0;
    private final int FILL_COLOR = new Color(0, 255, 0, 32).getRGB();

    public HitCooldownEntitiy(Module<HitCooldownData> module) {
        super(module);
    }

    private static final DecimalFormat format = new DecimalFormat("#.##");

    @OnlyIn(Dist.CLIENT)
    public static void clientTicksUpdate(int invulnerableTicks) {
        invulnerableTicksClient = invulnerableTicks;
        lastProgress = 1;
    }

    @Override
    public void tick(ModuleContext context) {
        if(EffectiveSide.get().isClient()) {
            if(invulnerableTicksClient > 0) invulnerableTicksClient--;
        }
        if(invulnerableTicks >= 0) {
            invulnerableTicks--;
        }
    }

    private static double lastProgress = 0;

    @Override
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        if(lastProgress > 0.01) {
            MatrixStack mStack = new MatrixStack();
            HitCooldownData data = (HitCooldownData) module.getData();
            double progress = ((double) invulnerableTicksClient) / data.getHitCooldownTicks();
            lastProgress = NikesMath.lerp(lastProgress, progress, 0.1f);
            GuiHelper.drawRect(getter, mStack, x, (y + height) - (height * lastProgress), width, height * lastProgress, FILL_COLOR);

        }
    }

    @Override
    public void addToolTip(List<ITextComponent> list) {
        HitCooldownData data = (HitCooldownData) module.getData();
        list.add(TranslationUtils.string(TextFormatting.GRAY +  TranslationUtils.getTranslation("module.extramodules2.hit_cooldown.cooldown") + ": " + TextFormatting.GREEN + format.format(data.getHitCooldownTicks()  / 20D) + "s"));
        if(invulnerableTicks > 0) {
            list.add(TranslationUtils.string(TextFormatting.GRAY + "" + ((float) invulnerableTicks / 20) + "s"));
        }
    }

    public void damaged(ServerPlayerEntity serverPlayerEntity, LivingAttackEvent event) {
        if(invulnerableTicks > 0) {
            event.setCanceled(true);
            return;
        }
        ShieldControlEntity shield = host.getEntitiesByType(ModuleTypes.SHIELD_CONTROLLER).map(e -> (ShieldControlEntity) e).findAny().orElse(null);
        if(shield == null) return;
        if(shield.isShieldEnabled() && shield.getShieldPoints() > 0) {
            HitCooldownData hitCooldownData = (HitCooldownData) module.getData();
            invulnerableTicks = Math.round(hitCooldownData.getHitCooldownTicks());
            EMNetwork.sendHitCooldownUpdate(serverPlayerEntity, invulnerableTicks);
        }
    }

    @Override
    public void writeToItemStack(ItemStack stack, ModuleContext context) {
        super.writeToItemStack(stack, context);
        stack.getOrCreateTag().putInt("invulnerable", invulnerableTicks);
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if (stack.hasTag()) {
            this.invulnerableTicks = stack.getOrCreateTag().getInt("invulnerable");
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putInt("invulnerable", invulnerableTicks);
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.invulnerableTicks = compound.getInt("invulnerable");
    }
}
