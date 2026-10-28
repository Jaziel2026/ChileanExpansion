package com.jaziel.chilean_expansion.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties MAQUI_BAYAS = new FoodProperties.Builder()
            .nutrition(2).saturationMod(1.5f).build();

    // En Minecraft la saturación final = nutrición * saturationMod * 2
    // 6 * 0.17 * 2 = ~2 de saturación final
    public static final FoodProperties COPIHUE_FRUTO = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.17f).build();
}