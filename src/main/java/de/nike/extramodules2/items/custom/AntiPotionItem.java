package de.nike.extramodules2.items.custom;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class AntiPotionItem extends Item {

	public AntiPotionItem(Properties p_i48487_1_) {
		super(p_i48487_1_);
	}

	public ItemStack finishUsingItem(ItemStack p_77654_1_, World p_77654_2_, LivingEntity p_77654_3_) {
		PlayerEntity playerentity = p_77654_3_ instanceof PlayerEntity ? (PlayerEntity) p_77654_3_ : null;
		if (playerentity instanceof ServerPlayerEntity) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) playerentity, p_77654_1_);
		}

		if (!p_77654_2_.isClientSide) {
			for (EffectInstance effectinstance : playerentity.getActiveEffects().toArray(new EffectInstance[playerentity.getActiveEffects().size()])) {
				if (!effectinstance.getEffect().isBeneficial()) {
					playerentity.removeEffect(effectinstance.getEffect());
				}
			}
		}

		if (playerentity != null) {
			playerentity.awardStat(Stats.ITEM_USED.get(this));
			if (!playerentity.abilities.instabuild) {
				p_77654_1_.shrink(1);
			}
		}

		if (playerentity == null || !playerentity.abilities.instabuild) {
			if (p_77654_1_.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (playerentity != null) {
				playerentity.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return p_77654_1_;
	}

	public int getUseDuration(ItemStack p_77626_1_) {
		return 32;
	}

	public UseAction getUseAnimation(ItemStack p_77661_1_) {
		return UseAction.DRINK;
	}

	public ActionResult<ItemStack> use(World world, PlayerEntity p_77659_2_, Hand p_77659_3_) {
		return DrinkHelper.useDrink(world, p_77659_2_, p_77659_3_);
	}

	@Override
	public boolean isFoil(ItemStack p_77636_1_) {
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack p_77616_1_) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> lore, ITooltipFlag flags) {
		lore.add(new TranslationTextComponent("item.extramodules2.anti_potion.lore"));
	}
}
