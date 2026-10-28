package com.jaziel.chilean_expansion.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModRegion extends Region {
    public ModRegion(ResourceLocation name) {
        // Peso 4: Asegura que tenga una buena probabilidad de aparecer al competir con biomas vanilla
        super(name, RegionType.OVERWORLD, 4);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {

            // SELVA VALDIVIANA: Alta humedad y temperatura templada
            builder.replaceBiome(Biomes.DARK_FOREST, ModBiomes.SELVA_VALDIVIANA);
            builder.replaceBiome(Biomes.OLD_GROWTH_PINE_TAIGA, ModBiomes.SELVA_VALDIVIANA);
            builder.replaceBiome(Biomes.OLD_GROWTH_SPRUCE_TAIGA, ModBiomes.SELVA_VALDIVIANA);

        });
    }
}