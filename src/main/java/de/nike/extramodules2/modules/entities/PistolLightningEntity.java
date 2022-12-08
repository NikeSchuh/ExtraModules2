package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.api.render.GuiHelper;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.modules.data.PistolLightningData;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.NikesMath;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class PistolLightningEntity extends ModuleEntity {

    private static final int FILL_COLOR = new Color(0, 255, 0, 40).getRGB();
    private static final int DISABLED_COLOR = new Color(255, 0, 0, 18).getRGB();

    private BooleanProperty activated;
    private int charge = 0;

    public PistolLightningEntity(Module<PistolLightningData> module) {
        super(module);
        addProperty(activated = new BooleanProperty("pistol_lightning_mod.lightning_activated", true).setFormatter(ConfigProperty.BooleanFormatter.ENABLED_DISABLED));
        this.savePropertiesToItem = true;
    }

    @Override
    public void tick(ModuleContext context) {
        if(!activated.getValue()) return;
        if(!(context instanceof StackModuleContext)) return;
        StackModuleContext stackModuleContext = (StackModuleContext) context;
        PistolLightningData pistolLightningData = (PistolLightningData) module.getData();
        boolean playSound = true;
        if(EffectiveSide.get().isServer()) {
            if(!isCharged()) {
                playSound = false;
                IOPStorageModifiable modifiable = context.getOpStorage();
                if(modifiable.getEnergyStored() > pistolLightningData.getChargeTickCost()) {
                    charge++;
                    modifiable.modifyEnergyStored(-pistolLightningData.getChargeTickCost());
                }
            }
            if(isCharged() && playSound == false) {
                playSound(stackModuleContext);
            }
        }
    }

    public void playSound(StackModuleContext context) {
        if(context.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) context.getEntity();
            serverPlayerEntity.playNotifySound(SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.MASTER, 0.5F, 1.50F);
        }
    }

    public void setCharge(int chargeTicks) {
        charge = chargeTicks;
    }

    public boolean isCharged() {
        return charge >= ((PistolLightningData)module.getData()).getChargeTicks() && activated.getValue();
    }

    private static final DecimalFormat format = new DecimalFormat("#.##");

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        MatrixStack mStack = new MatrixStack();
        PistolLightningData pistolLightningData = (PistolLightningData) module.getData();
        float progress = Math.max(0, Math.min(1, (float) charge / pistolLightningData.getChargeTicks()));
        if(progress < 1.0F) GuiHelper.drawRect(getter, mStack, x, y, width, height * (1 - progress), PistolLightningEntity.DISABLED_COLOR);
        if(progress >= 1.0f) return;
        GuiHelper.drawRect(getter, mStack, x, (y + height) - (height * progress), width, height * progress, FILL_COLOR);
        super.renderSlotOverlay(getter, mc, x, y, width, height, mouseX, mouseY, mouseOver, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addToolTip(List<ITextComponent> list) {
        PistolLightningData data = (PistolLightningData) module.getData();
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.pistol_lightning.chance") + ": " + TextFormatting.GREEN  + format.format(data.getChance() * 100) + "%"));
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.pistol_lightning.dmgcon") + ": " + TextFormatting.GREEN  + format.format(data.getSurroundingDamage() * 100) + "%"));
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.pistol_lightning.range") + ": " + TextFormatting.GREEN  + data.getRange() + " " +  TranslationUtils.getTranslation("module.extramodules2.pistol_lightning.block")));
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.pistol_lightning.cooldown") + ": " + TextFormatting.GREEN  + format.format(data.getChargeTicks() / 20F) + "s"));
        list.add(TranslationUtils.string(TextFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.pistol_lightning.cost") + ": " + TextFormatting.GREEN  + FormatUtils.formatE(data.getChargeTickCost()) + " OP/t"));
        super.addToolTip(list);
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if (stack.hasTag()) {
            this.charge = stack.getOrCreateTag().getInt("charge");
        }
    }

    @Override
    public void writeToItemStack(ItemStack stack, ModuleContext context) {
        super.writeToItemStack(stack, context);
        if(stack.hasTag()) {
            stack.getOrCreateTag().putInt("charge", charge);
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putInt("charge", charge);
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.charge = compound.getInt("charge");
    }
}
