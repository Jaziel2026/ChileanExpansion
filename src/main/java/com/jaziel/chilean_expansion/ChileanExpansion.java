package com.jaziel.chilean_expansion;

import com.jaziel.chilean_expansion.block.ModBlocks;
import com.jaziel.chilean_expansion.item.ModCreativeTab;
import com.jaziel.chilean_expansion.item.ModItems;
import com.jaziel.chilean_expansion.worldgen.ModFeatures;
import com.jaziel.chilean_expansion.worldgen.biome.ModRegion;
import com.jaziel.chilean_expansion.worldgen.biome.ModSurfaceRules;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

// El valor aquí debe coincidir con el mod_id en gradle.properties
@Mod(ChileanExpansion.MOD_ID)
public class ChileanExpansion {
    public static final String MOD_ID = "chilean_expansion";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ChileanExpansion() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registra el método commonSetup para la inicialización general del mod
        modEventBus.addListener(this::commonSetup);
        // Registra el bus de eventos de Forge para eventos del servidor y del juego
        MinecraftForge.EVENT_BUS.register(this);
        // Registra el método para agregar items a las pestañas del modo creativo
        modEventBus.addListener(this::addCreative);

        // Aquí se registrarán los DeferredRegisters (ModItems.register(modEventBus), etc.)
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModFeatures.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Registro directo de TerraBlender para Forge 1.20.1
            Regions.register(new ModRegion(new ResourceLocation("chilean_expansion", "overworld")));
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, "chilean_expansion", ModSurfaceRules.makeRules());
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Aquí se agregarán los items simples a las pestañas creativas
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Lógica al iniciar el servidor
    }

    // Clase estática para eventos exclusivos del cliente
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                // Propiedad "pull" (calcula cuánto se ha tensado el arco)
                ItemProperties.register(ModItems.MAQUI_ARCO.get(), new ResourceLocation("pull"),
                        (stack, level, entity, seed) -> {
                            if (entity == null) {
                                return 0.0F;
                            } else {
                                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 30.0F;
                            }
                        });

                // Propiedad "pulling" (detecta si el jugador está usando el arco)
                ItemProperties.register(ModItems.MAQUI_ARCO.get(), new ResourceLocation("pulling"),
                        (stack, level, entity, seed) -> {
                            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
                        });
            });
        }
    }
}