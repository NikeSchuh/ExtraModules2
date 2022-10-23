package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.api.render.GuiHelper;
import com.brandon3055.brandonscore.client.utils.GuiHelperOld;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.client.render.item.ToolRenderBase;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.DETags;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.modules.data.GeneratorData;
import de.nike.extramodules2.modules.data.OxygenStorageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.awt.*;

public class GeneratorEntity extends ModuleEntity {

    private final int FILL_COLOR = new Color(0, 255, 0, 32).getRGB();
    private final int TRANSPARENT = new Color(255, 255, 255, 0).getRGB();
    private final int FIRE_COLOR = new Color(255, 100, 0, 200).getRGB();

    private int burnTime = 0;
    private BooleanProperty active;
    private BooleanProperty generatorSounds;
    private int maxBurnTime = 0;

    public GeneratorEntity(Module<GeneratorData> module) {
        super(module);
        addProperty(active = new BooleanProperty("generator.active", true).setFormatter(ConfigProperty.BooleanFormatter.ENABLED_DISABLED));
        addProperty(generatorSounds = new BooleanProperty("generator.sounds", true).setFormatter(ConfigProperty.BooleanFormatter.ENABLED_DISABLED));
        this.savePropertiesToItem = true;
    }

    @Override
    public void tick(ModuleContext context) {
        if(!active.getValue()) return;
        if(context instanceof StackModuleContext) {
            if(EffectiveSide.get().isServer()) {
                StackModuleContext moduleContext = (StackModuleContext) context;
                if(moduleContext.isEquipped()) {
                    if(moduleContext.getEntity() instanceof ServerPlayerEntity) {
                        ServerPlayerEntity playerEntity = (ServerPlayerEntity) moduleContext.getEntity();
                        GeneratorData generatorData = (GeneratorData) module.getData();

                        IOPStorageModifiable energyStorage = moduleContext.getOpStorage();
                        if(energyStorage == null) {
                            return;
                        }

                        if(burnTime <= 0) {
                            if (playerEntity.tickCount % 20 == 0) {
                                for(ItemStack stack : playerEntity.inventory.items) {
                                    if(stack.isEmpty()) continue;
                                    if(stack.getItem() == DEContent.chaos_frag_large) {
                                        stack.shrink(1);
                                        burnTime = 100000;
                                        maxBurnTime = burnTime;
                                        playerEntity.sendMessage(new TranslationTextComponent("messages.extramodules2.generator.consumed"), ChatType.GAME_INFO, null);
                                        if(generatorSounds.getValue()) {
                                            playerEntity.level.playSound(null, playerEntity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1.2f);
                                        }
                                        break;
                                    } else if(stack.getItem() == Items.COAL) {
                                        stack.shrink(1);
                                        burnTime = 100;
                                        maxBurnTime = burnTime;
                                        if(generatorSounds.getValue()) {
                                            playerEntity.level.playSound(null, playerEntity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1.2f);
                                        }
                                        break;
                                    } else if(stack.getItem() == Items.COAL_BLOCK) {
                                        stack.shrink(1);
                                        burnTime = 100 * 9;
                                        maxBurnTime = burnTime;
                                        if(generatorSounds.getValue()) {
                                            playerEntity.level.playSound(null, playerEntity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1.2f);
                                        }
                                        break;
                                    } else if(stack.getItem() == EMItems.GENERATOR_FUEL.get()) {
                                        stack.shrink(1);
                                        burnTime = 100 * 9 * 8 * 4;
                                        maxBurnTime = burnTime;
                                        if(generatorSounds.getValue()) {
                                            playerEntity.level.playSound(null, playerEntity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1.2f);
                                        }
                                        break;
                                    }
                                }
                            }
                        } else {
                            if(energyStorage.getEnergyStored() + generatorData.getOpGeneration() <= energyStorage.getMaxEnergyStored()) {
                                energyStorage.modifyEnergyStored(generatorData.getOpGeneration());
                                burnTime--;
                                if(generatorSounds.getValue()) {
                                if(Math.random() < 0.01) {
                                    playerEntity.level.playSound(null, playerEntity.blockPosition(), SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1f, 1.8f);
                                }
                                }
                            }
                        }
                    }


                }
            }
        }
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderSlotOverlay(IRenderTypeBuffer getter, Minecraft mc, int x, int y, int width, int height, double mouseX, double mouseY, boolean mouseOver, float partialTicks) {
        MatrixStack mStack = new MatrixStack();
        //GuiHelper.drawRect(getter, mStack, x, y, width, height, 0x20FF0000);
        if(maxBurnTime <= 0) {
            maxBurnTime = burnTime;
        }
        if(burnTime > 0) {
        double progress = Math.max(0.0001d, ((double) burnTime) / maxBurnTime);
        GuiHelper.drawRect(getter, mStack, x, (y + height) - (height * progress), width, height * progress, FILL_COLOR);
        GuiHelper.drawRect(getter, mStack, x + 16, y + 16, 1.8,4.8, FIRE_COLOR);
        GuiHelper.drawRect(getter, mStack, x + 19.3, y + 16, 1.8,4.8, FIRE_COLOR);
        GuiHelper.drawRect(getter, mStack, x + 12.8, y + 16, 1.8,4.8, FIRE_COLOR);
        }

        if(burnTime > 0) {
            int minutes = (burnTime / 20 / 60);
            String pText = minutes + "m";
            if(minutes == 0) pText = (burnTime / 20) + "s";
            drawBackgroundString(getter, mStack, mc.font, pText, x + width / 2F, y + height / 2F + 5, 0, TRANSPARENT, 1, false, true);
        } else {
            drawBackgroundString(getter, mStack, mc.font, "Fuel!", x + width / 2F, y + height / 2F + 5, Color.red.getRGB(), TRANSPARENT, 1, false, true);
        }
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
        stack.getOrCreateTag().putInt("burntime", burnTime);
        stack.getOrCreateTag().putInt("maxburntime", maxBurnTime);
    }

    @Override
    public void readFromItemStack(ItemStack stack, ModuleContext context) {
        super.readFromItemStack(stack, context);
        if (stack.hasTag()) {
            this.burnTime = stack.getOrCreateTag().getInt("burntime");
            this.maxBurnTime = stack.getOrCreateTag().getInt("maxburntime");
        }
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        compound.putInt("burntime", burnTime);
        compound.putInt("maxburntime", maxBurnTime);
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        this.burnTime = compound.getInt("burntime");
        this.maxBurnTime = compound.getInt("maxburntime");
    }
}
