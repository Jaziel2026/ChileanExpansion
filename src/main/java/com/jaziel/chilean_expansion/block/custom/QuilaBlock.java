package com.jaziel.chilean_expansion.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class QuilaBlock extends BushBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public QuilaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new Vec3(0.65D, 0.85D, 0.65D));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);

        // 1. Expansión Horizontal: Reducida drásticamente (de 10 a 60)
        if (random.nextInt(60) == 0) {
            BlockPos spreadPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            if (spreadPos != pos && level.isEmptyBlock(spreadPos) && this.canSurvive(state, level, spreadPos)) {
                level.setBlock(spreadPos, this.defaultBlockState(), 3);
            }
        }

        // 2. Crecimiento Vertical Original (Máximo 3 bloques)
        if (level.isEmptyBlock(pos.above())) {
            int height = 1;
            while (level.getBlockState(pos.below(height)).is(this)) {
                height++;
            }
            if (height < 3) {
                // Añadimos una barrera de 1 en 25 para ralentizar el cambio de edad y el crecimiento
                if (random.nextInt(25) == 0) {
                    if (age == 1) {
                        level.setBlockAndUpdate(pos.above(), this.defaultBlockState());
                        level.setBlock(pos, state.setValue(AGE, 0), 4);
                    } else {
                        level.setBlock(pos, state.setValue(AGE, 1), 4);
                    }
                }
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.PODZOL) || state.is(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
