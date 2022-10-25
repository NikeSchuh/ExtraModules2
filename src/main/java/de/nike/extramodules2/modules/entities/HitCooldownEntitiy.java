package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.render.GuiHelper;
import com.brandon3055.brandonscore.utils.MathUtils;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.nike.extramodules2.modules.data.HitCooldownData;
import de.nike.extramodules2.utils.NikesMath;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class HitCooldownEntitiy extends ModuleEntity {

    private int invulnerableTicks = 0;
    private final int FILL_COLOR = new Color(0, 255, 0, 32).getRGB();

    public HitCooldownEntitiy(Module<HitCooldownData> module) {
        super(module);
    }

    private static final DecimalFormat format = new DecimalFormat("#.##");

    @Override
    public void tick(ModuleContext context) {
        if(invulnerableTicks > 0) {
            invulnerableTicks--;
            if(context instanceof StackModuleContext) {
                StackModuleContext stackModuleContext = (StackModuleContext) context;
                if(stackModuleContext.getEntity() instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) stackModuleContext.getEntity();
                    playerEntity.sendMessage(TranslationUtils.string(TextFormatting.RED + "" +  MathUtils.round(invulnerableTicks / 20D, 10) + "s"), ChatType.GAME_INFO, null);
                }
            }
        }
    }

    private double lastProgress = 0;

    @Override
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        MatrixStack mStack = new MatrixStack();
        HitCooldownData data = (HitCooldownData) module.getData();
        double target = Math.max(0.0001d, ((double) invulnerableTicks) / (data.getHitCooldownSeconds() * 20D));
        double current = NikesMath.lerp(lastProgress, target, 0.05f);
        lastProgress = current;
        GuiHelper.drawRect(getter, mStack, x, (y + height) - (height * current), width, height * current, FILL_COLOR);

    }

    @Override
    public void addToolTip(List<ITextComponent> list) {
        HitCooldownData data = (HitCooldownData) module.getData();
        list.add(TranslationUtils.string(TextFormatting.GRAY +  TranslationUtils.getTranslation("module.extramodules2.hit_cooldown.cooldown") + ": " + TextFormatting.GREEN + data.getHitCooldownSeconds() + "s"));
        if(invulnerableTicks > 0) {
            list.add(TranslationUtils.string(TextFormatting.GRAY + "" + ((float) invulnerableTicks / 20) + "s"));
        }
    }

    public void damaged(ServerPlayerEntity serverPlayerEntity, LivingAttackEvent event) {
        if(invulnerableTicks > 0) {
            event.setCanceled(true);
            return;
        }
        HitCooldownData hitCooldownData = (HitCooldownData) module.getData();
        invulnerableTicks = Math.round(hitCooldownData.getHitCooldownSeconds() * 20);
        System.out.println("invulnerable ticks set to " + invulnerableTicks);
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
