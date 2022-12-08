package de.nike.extramodules2.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BalancingConfig {

    public static ForgeConfigSpec.IntValue DRACONIC_GENERATOR_PRODUCTION;
    public static ForgeConfigSpec.IntValue CHAOTIC_GENERATOR_PRODUCTION;

    public static ForgeConfigSpec.IntValue WYVERN_OXYGEN_STORAGE;
    public static ForgeConfigSpec.IntValue DRACONIC_OXYGEN_STORAGE;
    public static ForgeConfigSpec.IntValue CHAOTIC_OXYGEN_STORAGE;

    public static ForgeConfigSpec.IntValue WYVERN_RAGE_TICKCOST;
    public static ForgeConfigSpec.IntValue DRACONIC_RAGE_TICKCOST;
    public static ForgeConfigSpec.IntValue CHAOTIC_RAGE_TICKCOST;

    public static ForgeConfigSpec.DoubleValue POTION_CURER_COST_MULTIPLIER;


    public static void registerServerConfig(ForgeConfigSpec.Builder server) {
        server.comment("Module Balancing").push("modules");

        DRACONIC_GENERATOR_PRODUCTION = server.comment("Production (in OP/t) 0 - " + Integer.MAX_VALUE)
                .defineInRange("draconicGeneratorProduction", 2000, 1, Integer.MAX_VALUE);
        CHAOTIC_GENERATOR_PRODUCTION = server.comment("Production (in OP/t) 0 - " + Integer.MAX_VALUE)
                .defineInRange("chaoticGeneratorProduction", 12600, 1, Integer.MAX_VALUE);

        WYVERN_OXYGEN_STORAGE = server.comment("Oxygen Storage 0 - " + Integer.MAX_VALUE)
                .defineInRange("wyvernOxygenStorage", 2000, 1, Integer.MAX_VALUE);
        DRACONIC_OXYGEN_STORAGE = server.comment("Oxygen Storage 0 - " + Integer.MAX_VALUE)
                .defineInRange("draconicOxygenStorage", 4000, 1, Integer.MAX_VALUE);
        CHAOTIC_OXYGEN_STORAGE = server.comment("Oxygen Storage 0 - " + Integer.MAX_VALUE)
                .defineInRange("chaoticOxygenStorage", 10000, 1, Integer.MAX_VALUE);

        WYVERN_RAGE_TICKCOST = server.comment("OP Cost (in OP/t) 0 - " + Integer.MAX_VALUE)
                .defineInRange("wyvernRageTickCost", 5000, 1, Integer.MAX_VALUE);
        DRACONIC_RAGE_TICKCOST = server.comment("OP Cost (in OP/t) 0 - " + Integer.MAX_VALUE)
                .defineInRange("draconicRageTickCost", 25000, 1, Integer.MAX_VALUE);
        CHAOTIC_RAGE_TICKCOST = server.comment("OP Cost (in OP/t) 0 - " + Integer.MAX_VALUE)
                .defineInRange("chaoticRageTickCost", 100000, 1, Integer.MAX_VALUE);
        POTION_CURER_COST_MULTIPLIER = server.comment("Potion Cure OP Cost Multiplier 0 - 10000")
                .defineInRange("draconicPotionCurerCostMultiplier", 1.0D, 0D, 10000D);

    }

}
