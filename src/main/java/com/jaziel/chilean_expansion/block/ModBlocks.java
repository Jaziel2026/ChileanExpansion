package com.jaziel.chilean_expansion.block;

import com.jaziel.chilean_expansion.ChileanExpansion;
import com.jaziel.chilean_expansion.block.custom.AlerceSaplingBlock;
import com.jaziel.chilean_expansion.block.custom.CopihueBlock;
import com.jaziel.chilean_expansion.block.custom.MaquiHojasBlock;
import com.jaziel.chilean_expansion.block.custom.MaquiTroncoBlock;
import com.jaziel.chilean_expansion.block.custom.QuilaBlock;
import com.jaziel.chilean_expansion.item.ModItems;
import com.jaziel.chilean_expansion.worldgen.tree.AlerceTreeGrower;
import com.jaziel.chilean_expansion.worldgen.tree.MaquiTreeGrower;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ChileanExpansion.MOD_ID);

    public static final RegistryObject<Block> MAQUI_TRONCO = registerBlock("maqui_tronco",
            () -> new MaquiTroncoBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryObject<Block> MAQUI_HOJAS = registerBlock("maqui_hojas",
            () -> new MaquiHojasBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    public static final RegistryObject<Block> MAQUI_BROTE = registerBlock("maqui_brote",
            () -> new SaplingBlock(new MaquiTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noOcclusion()));

    public static final RegistryObject<Block> ALERCE_TRONCO = registerBlock("alerce_tronco",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                    .strength(6.0f, 6.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ALERCE_HOJAS = registerBlock("alerce_hojas",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_LEAVES)));

    public static final RegistryObject<Block> ALERCE_BROTE = registerBlock("alerce_brote",
            () -> new AlerceSaplingBlock(new AlerceTreeGrower(), BlockBehaviour.Properties.copy(Blocks.SPRUCE_SAPLING)));

    public static final RegistryObject<Block> ALERCE_MADERA = registerBlock("alerce_madera",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_WOOD)
                    .strength(6.0f, 6.0f)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> COPIHUE = registerBlock("copihue",
            () -> new CopihueBlock(BlockBehaviour.Properties.copy(Blocks.VINE)
                    .randomTicks()
                    .noCollission()
                    .noOcclusion()));

    public static final RegistryObject<Block> QUILA = registerBlock("quila",
            () -> new QuilaBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO)
                    .noCollission()
                    .randomTicks()
                    .strength(0.5F)
                    .sound(SoundType.BAMBOO)));

    public static final RegistryObject<Block> TUPA = registerBlock("tupa",
            () -> new net.minecraft.world.level.block.DoublePlantBlock(
                    net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.PEONY).noOcclusion().noCollission()));

    public static final RegistryObject<Block> PALOMITA = registerBlock("palomita",
            () -> new FlowerBlock(() -> net.minecraft.world.effect.MobEffects.REGENERATION, 5,
                    net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.DANDELION).noOcclusion().noCollission()));

    public static final RegistryObject<Block> MOSS_BLOCK_DARK = registerBlock("moss_block_dark",
            () -> new net.minecraft.world.level.block.Block(
                    net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.MOSS_BLOCK)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
