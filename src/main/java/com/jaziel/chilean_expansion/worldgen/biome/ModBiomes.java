package com.jaziel.chilean_expansion.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    // La llave que representa tu archivo JSON "selva_valdiviana"
    public static final ResourceKey<Biome> SELVA_VALDIVIANA = register("selva_valdiviana");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation("chilean_expansion", name));
    }
}