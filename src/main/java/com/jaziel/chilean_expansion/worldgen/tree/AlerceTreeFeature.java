package com.jaziel.chilean_expansion.worldgen.tree;

import com.jaziel.chilean_expansion.block.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AlerceTreeFeature extends Feature<NoneFeatureConfiguration> {
    public AlerceTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        int height = 18 + random.nextInt(6); // Aún más alto

        BlockState soil = level.getBlockState(origin.below());
        if (!soil.is(BlockTags.DIRT) && !soil.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) return false;

        for (int i = 0; i <= height + 2; i++) {
            BlockPos checkPos = origin.above(i);
            BlockState state = level.getBlockState(checkPos);
            if (!level.isEmptyBlock(checkPos) && !state.canBeReplaced() && !state.is(ModBlocks.ALERCE_BROTE.get()) && !state.is(BlockTags.LEAVES)) {
                return false;
            }
        }

        int flag = 19;
        BlockState logState = ModBlocks.ALERCE_TRONCO.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        BlockState leafState = ModBlocks.ALERCE_HOJAS.get().defaultBlockState().setValue(net.minecraft.world.level.block.LeavesBlock.DISTANCE, 1);

        // 1. Tronco principal
        for (int y = 0; y < height; y++) {
            level.setBlock(origin.above(y), logState, flag);
        }

        // 2. Copa superior (fina y cónica)
        int leafStart = height - (6 + random.nextInt(3));
        for (int y = leafStart; y <= height + 1; y++) {
            int radius = (y >= height - 2) ? 1 : 2;
            if (y == height + 1) radius = 0; // Solo la punta

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    // Recorte extremo para simular follaje ligero
                    if (Math.abs(x) == radius && Math.abs(z) == radius && radius > 0) continue; // Sin esquinas
                    if (radius == 2 && random.nextInt(2) == 0) continue; // 50% de huecos en la base de la copa

                    if (x == 0 && z == 0 && y < height) continue;

                    BlockPos leafPos = origin.offset(x, y, z);
                    if (level.isEmptyBlock(leafPos)) {
                        level.setBlock(leafPos, leafState, flag);
                    }
                }
            }
        }

        // Remate de la punta
        level.setBlock(origin.above(height), leafState, flag);
        level.setBlock(origin.above(height + 1), leafState, flag);

        // 3. Ramas inferiores aisladas (como en la fotografía)
        int numBranches = 1 + random.nextInt(3);
        for (int b = 0; b < numBranches; b++) {
            int branchY = 6 + random.nextInt(height - 12);
            Direction branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int length = 1 + random.nextInt(2);

            for (int l = 1; l <= length; l++) {
                BlockPos branchPos = origin.above(branchY).relative(branchDir, l);
                if (level.isEmptyBlock(branchPos) || level.getBlockState(branchPos).canBeReplaced()) {
                    level.setBlock(branchPos, logState.setValue(RotatedPillarBlock.AXIS, branchDir.getAxis()), flag);

                    // Pequeño cúmulo de hojas en la punta de la rama
                    if (l == length) {
                        for (Direction dir : Direction.values()) {
                            if (dir == branchDir.getOpposite()) continue;
                            if (random.nextInt(4) > 0) {
                                BlockPos leafPos = branchPos.relative(dir);
                                if (level.isEmptyBlock(leafPos)) level.setBlock(leafPos, leafState, flag);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
