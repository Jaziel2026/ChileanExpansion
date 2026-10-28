package com.jaziel.chilean_expansion.block.custom;

import com.jaziel.chilean_expansion.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopihueBlock extends Block {
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    protected static final VoxelShape WEST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    // 0: Enredadera, 1: Flor (3D), 2: Fruto (3D)
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    // 0: Rojo, 1: Rosa, 2: Blanco, 3: Colli
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 3);

    public static final TagKey<Block> SOPORTE = TagKey.create(Registries.BLOCK, new ResourceLocation("chilean_expansion", "soporte_copihue"));

    public CopihueBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(AGE, 0)
                .setValue(COLOR, 0)); // Por defecto rojo
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            default -> NORTH_AABB;
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos logPos = pos.relative(direction.getOpposite());
        return level.getBlockState(logPos).is(SOPORTE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        if (direction.getAxis().isHorizontal()) {
            // Asigna un color aleatorio al plantarlo manualmente (opcional, o puedes hacerlo mediante items distintos)
            int randomColor = context.getLevel().getRandom().nextInt(4);
            return this.defaultBlockState().setValue(FACING, direction).setValue(COLOR, randomColor);
        }
        return null;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // 1. Mecánica de trepar - 5% de probabilidad
        if (random.nextInt(20) == 0) {
            BlockPos posAbove = pos.above();
            if (level.isEmptyBlock(posAbove) && this.canSurvive(state, level, posAbove)) {
                level.setBlock(posAbove, this.defaultBlockState()
                        .setValue(FACING, state.getValue(FACING))
                        .setValue(COLOR, state.getValue(COLOR)), 2);
            }
        }

        int age = state.getValue(AGE);
        if (age < 2 && random.nextInt(25) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(AGE);
        if (age == 2) {
            // Cosechar el fruto (usando el item copihue_fruto_item)
            popResource(level, pos, new ItemStack(ModItems.COPIHUE_FRUTO.get(), 1));
            // Reinicia a enredadera (manteniendo color y dirección)
            level.setBlock(pos, state.setValue(AGE, 0), 2);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE, COLOR);
    }
}
