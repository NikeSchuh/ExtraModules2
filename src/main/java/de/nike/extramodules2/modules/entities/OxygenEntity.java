package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.render.GuiHelper;
import com.brandon3055.brandonscore.client.utils.GuiHelperOld;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.AutoFeedData;
import com.brandon3055.draconicevolution.api.modules.data.UndyingData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.client.render.item.ToolRenderBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sun.media.jfxmedia.logging.Logger;
import de.nike.extramodules2.modules.data.OxygenStorageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.system.CallbackI;

public class OxygenEntity extends ModuleEntity {

    public static final int OXYGEN_REFILL_RATE = 100;

    private BooleanProperty consumeOxygen;
    private int oxygenStored;

    public OxygenEntity(Module<OxygenStorageData> module) {
        super(module);
        addProperty(consumeOxygen = new BooleanProperty("oxygen_mod.consume_oxygen", true).setFormatter(ConfigProperty.BooleanFormatter.YES_NO));
        this.savePropertiesToItem = true;
    }

    @Override
    public void tick(ModuleContext context) {
        if(context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            LivingEntity entity = moduleContext.getEntity();
            if (entity instanceof ServerPlayerEntity && entity.tickCount % 10 == 0 && ((StackModuleContext) context).isEquipped()) {
                OxygenStorageData oxygenStorageData = (OxygenStorageData) module.getData();
                ServerPlayerEntity playerEntity = (ServerPlayerEntity) entity;
                int maxStorage = oxygenStorageData.getOxygenStored();
                if(playerEntity.isUnderWater()) {
                    int missingAir = playerEntity.getMaxAirSupply() - playerEntity.getAirSupply();
                    if(oxygenStored == 0) return;
                    if(oxygenStored > missingAir) {
                        playerEntity.setAirSupply(playerEntity.getMaxAirSupply());
                        oxygenStored-=missingAir;
                    } else {
                        playerEntity.setAirSupply(playerEntity.getAirSupply() + oxygenStored);
                        oxygenStored = 0;
                        playerEntity.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1f, 1f);
                    }
                } else {
                    if(oxygenStored < maxStorage) {
                        oxygenStored += Math.min(OXYGEN_REFILL_RATE, maxStorage - oxygenStored);
                    }
                }
            }
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        OxygenStorageData data = ((OxygenStorageData) module.getData());
        if(oxygenStored >= data.getOxygenStored()) return;
        MatrixStack mStack = new MatrixStack();
        GuiHelper.drawRect(getter, mStack, x, y, width, height, 0x20FF0000);

        double diameter = Math.min(width, height) * 0.425;
        double progress = oxygenStored / Math.max(1D, data.getOxygenStored());

        //GuiHelper.drawRect(getter, mStack, x, y, width, height, 0x20FF0000);
        IVertexBuilder builder = getter.getBuffer(GuiHelperOld.FAN_TYPE);
        builder.vertex(x + (width / 2D), y + (height / 2D), 0).color(0, 255, 255, 64).endVertex();
        for (double d = 0; d <= 1; d += 1D / 30D) {
            double angle = (d * progress) + 0.5 - progress;
            double vertX = x + (width / 2D) + Math.sin(angle * (Math.PI * 2)) * diameter;
            double vertY = y + (height / 2D) + Math.cos(angle * (Math.PI * 2)) * diameter;
            builder.vertex(vertX, vertY, 0).color(0, 255, 0, 64).endVertex();
        }
        ToolRenderBase.endBatch(getter);

      //  String pText = oxygenStored + "";
      //  String tText = ((int) (data.getOxygenStored() - oxygenStored) / (OXYGEN_REFILL_RATE * 2)) + "s";
       // drawBackgroundString(getter, mStack, mc.font, pText, x + width / 2F, y + height / 2F - 3, 0, 0x4000FF00, 1, false, true);
       // drawBackgroundString(getter, mStack, mc.font, tText, x + width / 2F, y + height / 2F + 7, 0, 0x4000FF00, 1, false, true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawBackgroundString(IRenderTypeBuffer getter, MatrixStack mStack, FontRenderer font, String text, float x, float y, int colour, int background, int padding, boolean shadow, boolean centered) {
        MatrixStack matrixstack = new MatrixStack();
        int width = font.width(text);
        x = centered ? x - width / 2F : x;
        GuiHelper.drawRect(getter, mStack, x - padding, y - padding, width + padding * 2, font.lineHeight - 2 + padding * 2, background);
        font.drawInBatch(text, x, y, colour, shadow, matrixstack.last().pose(), getter, false, 0, 15728880);
    }


    @Override
    public void writeToItemStack(ItemStack stack, ModuleContext context) {
        super.writeToItemStack(stack, context);
        stack.getOrCreateTag().putInt("oxygen_stored", oxygenStored);
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if (stack.hasTag()) {
            this.oxygenStored = stack.getOrCreateTag().getInt("oxygen_stored");
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putInt("oxygen_stored", oxygenStored);
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.oxygenStored = compound.getInt("oxygen_stored");
    }
}
