package com.jaziel.chilean_expansion.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class MaquiTroncoBlock extends PipeBlock {
    public MaquiTroncoBlock(Properties properties) {
        super(0.21875f, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false)
                .setValue(UP, false).setValue(DOWN, true));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        boolean connectsDown = this.connectsTo(level.getBlockState(pos.below())) || level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);

        return this.defaultBlockState()
                .setValue(DOWN, connectsDown)
                .setValue(UP, this.connectsTo(level.getBlockState(pos.above())))
                .setValue(NORTH, this.connectsTo(level.getBlockState(pos.north())))
                .setValue(EAST, this.connectsTo(level.getBlockState(pos.east())))
                .setValue(SOUTH, this.connectsTo(level.getBlockState(pos.south())))
                .setValue(WEST, this.connectsTo(level.getBlockState(pos.west())));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        boolean connects = this.connectsTo(facingState);
        if (facing == Direction.DOWN && !connects) {
            connects = facingState.isFaceSturdy(level, facingPos, Direction.UP);
        }
        return state.setValue(PROPERTY_BY_DIRECTION.get(facing), connects);
    }

    public boolean connectsTo(BlockState state) {
        return state.getBlock() instanceof MaquiTroncoBlock || state.getBlock() instanceof MaquiHojasBlock;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }
}