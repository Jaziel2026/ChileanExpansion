package com.jaziel.chilean_expansion.worldgen.biome;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource isValdivian = SurfaceRules.isBiome(ModBiomes.SELVA_VALDIVIANA);

        // Regla para el bloque superior (Solo pasto, el resto lo hacemos con features)
        SurfaceRules.RuleSource valdivianTopSurface = GRASS_BLOCK;

        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(
                SurfaceRules.ifTrue(isAtOrAboveWaterLevel, valdivianTopSurface),
                DIRT
        );

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(),
                        SurfaceRules.sequence(
                                // 1. Capa superior (ON_FLOOR)
                                SurfaceRules.ifTrue(isValdivian,
                                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassSurface)
                                ),
                                // 2. Capa inferior de tierra (UNDER_FLOOR)
                                SurfaceRules.ifTrue(isValdivian,
                                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT)
                                )
                        )
                )
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}