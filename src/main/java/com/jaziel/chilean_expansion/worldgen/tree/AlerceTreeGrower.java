package com.jaziel.chilean_expansion.worldgen.tree;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class AlerceTreeGrower extends AbstractMegaTreeGrower {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ALERCE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, new ResourceLocation("chilean_expansion", "alerce")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRAN_ALERCE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, new ResourceLocation("chilean_expansion", "gran_alerce")
    );

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        return ALERCE_KEY; // Árbol 1x1
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
        return GRAN_ALERCE_KEY; // Árbol 2x2
    }
}