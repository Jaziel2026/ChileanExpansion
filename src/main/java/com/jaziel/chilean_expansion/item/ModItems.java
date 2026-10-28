package com.jaziel.chilean_expansion.item;

import com.jaziel.chilean_expansion.ChileanExpansion;
import com.jaziel.chilean_expansion.item.custom.MaquiArcoItem;
import com.jaziel.chilean_expansion.item.custom.MaquiCuerdaItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ChileanExpansion.MOD_ID);

    // Alimentos
    public static final RegistryObject<Item> MAQUI_BAYAS = ITEMS.register("maqui_bayas",
            () -> new Item(new Item.Properties().food(ModFoods.MAQUI_BAYAS)));

    public static final RegistryObject<Item> COPIHUE_FRUTO = ITEMS.register("copihue_fruto",
            () -> new Item(new Item.Properties().food(ModFoods.COPIHUE_FRUTO)));

    // Materiales de crafteo
    public static final RegistryObject<Item> MAQUI_FIBRA = ITEMS.register("maqui_fibra",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAQUI_VARA = ITEMS.register("maqui_vara",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAQUI_CUERDA = ITEMS.register("maqui_cuerda",
            () -> new MaquiCuerdaItem(new Item.Properties()));

    // Armas
    public static final RegistryObject<Item> MAQUI_ARCO = ITEMS.register("maqui_arco",
            () -> new MaquiArcoItem(new Item.Properties().durability(384)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
