package com.jaziel.chilean_expansion.worldgen.tree;

import com.jaziel.chilean_expansion.block.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GranAlerceTreeFeature extends Feature<NoneFeatureConfiguration> {
    public GranAlerceTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        // Altura colosal: 40 a 55 bloques (simulando los 45-60 metros)
        int height = 40 + random.nextInt(16);

        for (int dx = 0; dx <= 1; dx++) {
            for (int dz = 0; dz <= 1; dz++) {
                BlockState soil = level.getBlockState(origin.offset(dx, -1, dz));
                if (!soil.is(BlockTags.DIRT) && !soil.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) {
                    return false;
                }
            }
        }

        int flag = 19;
        BlockState logState = ModBlocks.ALERCE_TRONCO.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        BlockState leafState = ModBlocks.ALERCE_HOJAS.get().defaultBlockState().setValue(LeavesBlock.DISTANCE, 1);

        // 1. Tronco masivo 2x2
        for (int y = 0; y < height; y++) {
            level.setBlock(origin.offset(0, y, 0), logState, flag);
            level.setBlock(origin.offset(1, y, 0), logState, flag);
            level.setBlock(origin.offset(0, y, 1), logState, flag);
            level.setBlock(origin.offset(1, y, 1), logState, flag);
        }

        // 2. Copa superior (más delgada y ligera)
        int leafStart = height - (12 + random.nextInt(5));
        for (int y = leafStart; y <= height + 2; y++) {
            // Radio más pequeño que antes para quitar volumen
            int radius = (y >= height - 3) ? 1 : 2 + random.nextInt(2);
            if (y > height) radius = 0;

            for (int x = -radius; x <= radius + 1; x++) {
                for (int z = -radius; z <= radius + 1; z++) {
                    // Recortes más agresivos: elimina más esquinas y genera más huecos
                    if (Math.abs(x) >= radius && Math.abs(z) >= radius) {
                        if (random.nextInt(3) > 0) continue;
                    } else if (random.nextInt(4) == 0) {
                        continue;
                    }

                    if ((x == 0 || x == 1) && (z == 0 || z == 1) && y < height) continue;

                    BlockPos leafPos = origin.offset(x, y, z);
                    if (level.isEmptyBlock(leafPos)) {
                        level.setBlock(leafPos, leafState, flag);
                    }
                }
            }
        }

        // Puntas irregulares en el tope del 2x2
        for (int dx = 0; dx <= 1; dx++) {
            for (int dz = 0; dz <= 1; dz++) {
                if (random.nextBoolean()) {
                    level.setBlock(origin.offset(dx, height, dz), leafState, flag);
                    if (random.nextBoolean()) {
                        level.setBlock(origin.offset(dx, height + 1, dz), leafState, flag);
                    }
                }
            }
        }

        // 3. Ramas esporádicas (inician más alto acorde al nuevo tamaño)
        int numBranches = 4 + random.nextInt(5);
        for (int b = 0; b < numBranches; b++) {
            int branchY = 15 + random.nextInt(height - 22);
            Direction branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int length = 2 + random.nextInt(3);

            int startX = (branchDir == Direction.EAST) ? 1 : 0;
            int startZ = (branchDir == Direction.SOUTH) ? 1 : 0;
            BlockPos branchBase = origin.offset(startX, branchY, startZ);

            for (int l = 1; l <= length; l++) {
                BlockPos branchPos = branchBase.relative(branchDir, l);
                if (level.isEmptyBlock(branchPos) || level.getBlockState(branchPos).canBeReplaced()) {
                    level.setBlock(branchPos, logState.setValue(RotatedPillarBlock.AXIS, branchDir.getAxis()), flag);

                    // Follaje reducido a la mitad de densidad en las ramas
                    if (l >= length - 1) {
                        for (Direction dir : Direction.values()) {
                            if (random.nextBoolean()) {
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