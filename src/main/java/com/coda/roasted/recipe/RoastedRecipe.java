package com.coda.roasted.recipe;

import com.coda.roasted.Roasted;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class RoastedRecipe extends ShapedRecipe {

    public static final RecipeType<RoastedRecipe> RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return "roasted:roastedness";
        }
    };

    public RoastedRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) {
        super(id, group, width, height, recipeItems, result);
    }

    @Override
    public RecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Roasted.ROASTED_RECIPE_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<RoastedRecipe> {

        @Override
        public RoastedRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            return null;
        }

        @Override
        public RoastedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RoastedRecipe recipe) {

        }
    }
}
