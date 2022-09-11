package com.coda.roasted;

import com.coda.roasted.recipe.RoastedRecipe;
import com.coda.roasted.registry.BlockRegistry;
import com.coda.roasted.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;

public class Roasted implements ModInitializer {

    public static final String MODID = "roasted";

    @Override
    public void onInitialize() {
        BlockRegistry.init();
        ItemRegistry.init();
        ItemRegistry.registerCampfireMutations();
    }

    public static final RecipeSerializer<RoastedRecipe> ROASTED_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(MODID, "roasted_recipe"), new RoastedRecipe.Serializer());

    public static final TagKey<Block> SOUL_CAMPFIRES = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(MODID, "soul_campfires"));
}
