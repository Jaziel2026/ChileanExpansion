package com.jaziel.chilean_expansion.item;

import com.jaziel.chilean_expansion.ChileanExpansion;
import com.jaziel.chilean_expansion.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChileanExpansion.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CHILEAN_EXPANSION_TAB = CREATIVE_MODE_TABS.register("chilean_expansion_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.COPIHUE.get()))
                    .title(Component.translatable("creativetab.chilean_expansion_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.MAQUI_BAYAS.get());
                        output.accept(ModItems.MAQUI_FIBRA.get());
                        output.accept(ModItems.MAQUI_VARA.get());
                        output.accept(ModItems.MAQUI_CUERDA.get());
                        output.accept(ModItems.MAQUI_ARCO.get());
                        output.accept(ModBlocks.MAQUI_TRONCO.get());
                        output.accept(ModBlocks.MAQUI_HOJAS.get());
                        output.accept(ModBlocks.MAQUI_BROTE.get());
                        output.accept(ModBlocks.ALERCE_TRONCO.get());
                        output.accept(ModBlocks.ALERCE_HOJAS.get());
                        output.accept(ModBlocks.ALERCE_BROTE.get());
                        output.accept(ModBlocks.ALERCE_MADERA.get());
                        output.accept(ModBlocks.COPIHUE.get());
                        output.accept(ModItems.COPIHUE_FRUTO.get());
                        output.accept(ModBlocks.QUILA.get());
                        output.accept(ModBlocks.PALOMITA.get());
                        output.accept(ModBlocks.MOSS_BLOCK_DARK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}