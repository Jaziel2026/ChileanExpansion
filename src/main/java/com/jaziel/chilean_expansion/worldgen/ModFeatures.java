package com.jaziel.chilean_expansion.worldgen;

import com.jaziel.chilean_expansion.worldgen.tree.AlerceTreeFeature;
import com.jaziel.chilean_expansion.worldgen.tree.GranAlerceTreeFeature;
import com.jaziel.chilean_expansion.worldgen.tree.MaquiTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, "chilean_expansion");

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MAQUI_TREE =
            FEATURES.register("maqui_tree", () -> new MaquiTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ALERCE_TREE =
            FEATURES.register("alerce_tree", () -> new AlerceTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> GRAN_ALERCE_TREE =
            FEATURES.register("gran_alerce_tree", () -> new GranAlerceTreeFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}