package com.jaziel.chilean_expansion.worldgen.tree;

import com.jaziel.chilean_expansion.block.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MaquiTreeFeature extends Feature<NoneFeatureConfiguration> {
    public MaquiTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        int height = 3 + random.nextInt(2);
        BlockState soil = level.getBlockState(origin.below());
        if (!soil.is(BlockTags.DIRT) && !soil.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) return false;

        for (int i = 0; i <= height + 1; i++) {
            BlockPos checkPos = origin.above(i);
            BlockState state = level.getBlockState(checkPos);
            if (!level.isEmptyBlock(checkPos) && !state.canBeReplaced() && !state.is(ModBlocks.MAQUI_BROTE.get()) && !state.is(BlockTags.LEAVES)) {
                return false;
            }
        }

        int flag = 19;

        // 1. Construir el tronco principal conectando hojas dinámicamente
        for (int y = 0; y < height; y++) {
            BlockPos logPos = origin.above(y);
            boolean isTop = (y == height - 1);

            BlockState logState = ModBlocks.MAQUI_TRONCO.get().defaultBlockState()
                    .setValue(BlockStateProperties.DOWN, true)
                    .setValue(BlockStateProperties.UP, !isTop)
                    .setValue(BlockStateProperties.NORTH, false)
                    .setValue(BlockStateProperties.SOUTH, false)
                    .setValue(BlockStateProperties.EAST, false)
                    .setValue(BlockStateProperties.WEST, false);

            level.setBlock(logPos, logState, flag);

            if (y >= height - 2) {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos leafPos = logPos.relative(dir);
                    if (level.isEmptyBlock(leafPos)) {
                        level.setBlock(leafPos, ModBlocks.MAQUI_HOJAS.get().defaultBlockState(), flag);
                        logState = logState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), true);
                    }
                    if (random.nextBoolean()) {
                        BlockPos cornerPos = leafPos.relative(dir.getClockWise());
                        if (level.isEmptyBlock(cornerPos)) {
                            level.setBlock(cornerPos, ModBlocks.MAQUI_HOJAS.get().defaultBlockState(), flag);
                        }
                    }
                }
                level.setBlock(logPos, logState, flag); // Aplica ramas generadas
            } else if (y > 0) {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    if (random.nextInt(5) > 1) {
                        BlockPos leafPos = logPos.relative(dir);
                        if (level.isEmptyBlock(leafPos)) {
                            level.setBlock(leafPos, ModBlocks.MAQUI_HOJAS.get().defaultBlockState(), flag);
                            logState = logState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), true);
                        }
                    }
                }
                level.setBlock(logPos, logState, flag); // Aplica ramas generadas
            }
        }

        BlockPos topPos = origin.above(height);
        if (level.isEmptyBlock(topPos)) {
            level.setBlock(topPos, ModBlocks.MAQUI_HOJAS.get().defaultBlockState(), flag);
        }

        // 2. Generar ramas laterales secundarias y actualizar conexiones
        int numBranches = 1 + random.nextInt(3);
        for (int b = 0; b < numBranches; b++) {
            int branchY = 1 + random.nextInt(height - 2);
            Direction branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            BlockPos branchPos = origin.above(branchY).relative(branchDir);
            BlockState currentState = level.getBlockState(branchPos);

            if (level.isEmptyBlock(branchPos) || currentState.is(BlockTags.LEAVES) || currentState.canBeReplaced()) {
                BlockState branchState = ModBlocks.MAQUI_TRONCO.get().defaultBlockState()
                        .setValue(BlockStateProperties.DOWN, false)
                        .setValue(BlockStateProperties.UP, false)
                        .setValue(BlockStateProperties.NORTH, false)
                        .setValue(BlockStateProperties.SOUTH, false)
                        .setValue(BlockStateProperties.EAST, false)
                        .setValue(BlockStateProperties.WEST, false)
                        .setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(branchDir.getOpposite()), true); // Conecta al tronco

                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos branchLeafPos = branchPos.relative(dir);
                    if (level.isEmptyBlock(branchLeafPos)) {
                        level.setBlock(branchLeafPos, ModBlocks.MAQUI_HOJAS.get().defaultBlockState(), flag);
                        branchState = branchState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), true);
                    }
                }

                level.setBlock(branchPos, branchState, flag);

                // Conectar el tronco principal hacia la nueva rama lateral
                BlockPos mainLogPos = origin.above(branchY);
                BlockState mainLogState = level.getBlockState(mainLogPos);
                if (mainLogState.is(ModBlocks.MAQUI_TRONCO.get())) {
                    level.setBlock(mainLogPos, mainLogState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(branchDir), true), flag);
                }
            }
        }
        return true;
    }
}