package de.nike.extramodules2.modules;

import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.types.ModuleTypeImpl;
import de.nike.extramodules2.modules.data.*;
import de.nike.extramodules2.modules.entities.*;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;

public class EMModuleTypes {

	public static final ModuleType<OxygenStorageData> OXYGEN_STORAGE = new ModuleTypeImpl<>("oxygen_storage", 1, 1, OxygenEntity::new, new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST}).setMaxInstallable(1);
	public static final ModuleType<DefenseBrainData> DEFENSE_BRAIN = new ModuleTypeImpl<>("defense_brain", 2, 2, DefenseBrainEntity::new, new ModuleCategory[] {ModuleCategory.CHESTPIECE}).setMaxInstallable(1);
	public static final ModuleType<DefenseSystemData> DEFENSE_SYSTEM = new ModuleTypeImpl<>("defense_system", 1, 1, new ModuleCategory[] {ModuleCategory.CHESTPIECE}).setMaxInstallable(-1);
	public static final ModuleType<PotionCureData> POTION_CURER = new ModuleTypeImpl<>("potion_curer", 1, 2, PotionCureEntity::new, new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST}).setMaxInstallable(1);
	public static final ModuleType<GeneratorData> GENERATOR = new ModuleTypeImpl<>("op_generator", 2, 2, GeneratorEntity::new, new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST}).setMaxInstallable(1);
	public static final ModuleType<ArmorData> ARMOR  =new ModuleTypeImpl<>("armor", 1, 1, ArmorEntity::new, new ModuleCategory[] { ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR, ModuleCategory.ARMOR_CHEST}).setMaxInstallable(5);
	public static final ModuleType<ExtraHealthData> EXTRA_HEALTH = new ModuleTypeImpl<>("extra_health", 2, 1, ExtraHealthEntity::new, new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR, ModuleCategory.ARMOR_CHEST}).setMaxInstallable(2);
	public static final ModuleType<HitCooldownData> HIT_COOLDOWN = new ModuleTypeImpl<>("hit_cooldown", 1, 3, HitCooldownEntitiy::new, new ModuleCategory[] {ModuleCategory.CHESTPIECE}).setMaxInstallable(1);
	public static final ModuleType<EffectData> EFFECT = new ModuleTypeImpl<>("effect", 1, 2, new ModuleCategory[] {EMModuleCategories.EFFECT}).setMaxInstallable(-1);
	public static final ModuleType<PistolData> PISTOL = new ModuleTypeImpl<>("pistol", 1, 1, new ModuleCategory[] {EMModuleCategories.PISTOL}).setMaxInstallable(-1);
	public static final ModuleType<PistolLightningData> PISTOL_LIGHTNING = new ModuleTypeImpl<>("pistol_lightning", 2, 3, PistolLightningEntity::new, new ModuleCategory[] {EMModuleCategories.PISTOL}).setMaxInstallable(1);
	public static final ModuleType<PistolHomingData> PISTOL_HOMING = new ModuleTypeImpl<>("pistol_homing", 2, 2, new ModuleCategory[] {EMModuleCategories.PISTOL}).setMaxInstallable(1);

}
