package de.nike.extramodules2.modules;

import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.types.ModuleTypeImpl;
import de.nike.extramodules2.modules.data.*;
import de.nike.extramodules2.modules.entities.defensesystem.DefenseBrainEntity;
import de.nike.extramodules2.modules.entities.GeneratorEntity;
import de.nike.extramodules2.modules.entities.OxygenEntity;
import de.nike.extramodules2.modules.entities.PotionCureEntity;

public class ModuleTypes {

    public static final ModuleType<OxygenStorageData> OXYGEN_STORAGE =
            new ModuleTypeImpl<>("oxygen_storage",
                    1,
                    1,
                    OxygenEntity::new,
                    new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST })
                    .setMaxInstallable(1);
    public static final ModuleType<DefenseBrainData> DEFENSE_BRAIN =
            new ModuleTypeImpl<>("defense_brain",
                    2,
                    2,
                    DefenseBrainEntity::new,
                    new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST})
            .setMaxInstallable(1);
    public static final ModuleType<DefenseSystemData> DEFENSE_SYSTEM =
            new ModuleTypeImpl<>("defense_system",
                    1,
                    1,
                    new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST})
                    .setMaxInstallable(-1);
    public static final ModuleType<PotionCureData> POTION_CURER =
            new ModuleTypeImpl<>("potion_curer",
                    1,
                    2,
                    PotionCureEntity::new,
                    new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST})
                    .setMaxInstallable(1);

    public static final ModuleType<GeneratorData> GENERATOR =
            new ModuleTypeImpl<>("op_generator",
                    2,
                    2,
                    GeneratorEntity::new,
                    new ModuleCategory[] {ModuleCategory.CHESTPIECE, ModuleCategory.ARMOR_CHEST})
                    .setMaxInstallable(1);

}
