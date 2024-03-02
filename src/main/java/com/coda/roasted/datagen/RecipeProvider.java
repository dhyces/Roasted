package com.coda.roasted.datagen;

import com.coda.roasted.item.components.RoastInfo;
import com.coda.roasted.recipe.RoastedIngredient;
import com.coda.roasted.registry.DataComponentRegistry;
import com.coda.roasted.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.campfireCooking;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemRegistry.MARSHMALLOW)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .define('#', Items.SUGAR)
                .unlockedBy("has_sugar", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SUGAR))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemRegistry.MARSHMALLOW_STICK)
                .requires(Items.STICK)
                .requires(ItemRegistry.MARSHMALLOW)
                .unlockedBy("has_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.MARSHMALLOW))
                .save(exporter);

        smoreRecipe(ItemRegistry.UNROASTED_SMORE, ItemRegistry.MARSHMALLOW_STICK, 0, 5, exporter);
        smoreRecipe(ItemRegistry.WARM_SMORE, ItemRegistry.MARSHMALLOW_STICK, 5, 15, exporter);
        smoreRecipe(ItemRegistry.PERFECT_SMORE, ItemRegistry.MARSHMALLOW_STICK, 15, 20, exporter);
        smoreRecipe(ItemRegistry.BURNT_SMORE, ItemRegistry.MARSHMALLOW_STICK, 20, 30, exporter);
        smoreRecipe(ItemRegistry.CHARRED_SMORE, ItemRegistry.MARSHMALLOW_STICK, 30, 50, exporter);

        smoreRecipe(ItemRegistry.UNROASTED_SMORE, ItemRegistry.SOUL_MARSHMALLOW_STICK, 0, 5, exporter);
        smoreRecipe(ItemRegistry.WARM_SMORE, ItemRegistry.SOUL_MARSHMALLOW_STICK, 5, 15, exporter);
        smoreRecipe(ItemRegistry.PERFECT_SMORE, ItemRegistry.SOUL_MARSHMALLOW_STICK, 15, 20, exporter);
        smoreRecipe(ItemRegistry.BURNT_SMORE, ItemRegistry.SOUL_MARSHMALLOW_STICK, 20, 30, exporter);
        smoreRecipe(ItemRegistry.CHARRED_SMORE, ItemRegistry.SOUL_MARSHMALLOW_STICK, 30, 50, exporter);

        cutBuilder(RecipeCategory.BUILDING_BLOCKS, ItemRegistry.MARSHMALLOW_PILE, Ingredient.of(ItemRegistry.MARSHMALLOW))
                .group("roasted:marshmallow_pile")
                .unlockedBy("has_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.MARSHMALLOW))
                .save(exporter);
        campfireCooking(Ingredient.of(ItemRegistry.MARSHMALLOW_PILE), RecipeCategory.BUILDING_BLOCKS, ItemRegistry.WARM_MARSHMALLOW_PILE, 0.1f, 100)
                .group("roasted:marshmallow_pile")
                .unlockedBy("has_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.MARSHMALLOW))
                .save(exporter);
        campfireCooking(Ingredient.of(ItemRegistry.WARM_MARSHMALLOW_PILE), RecipeCategory.BUILDING_BLOCKS, ItemRegistry.PERFECT_MARSHMALLOW_PILE, 0.1f, 100)
                .group("roasted:marshmallow_pile")
                .unlockedBy("has_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.MARSHMALLOW))
                .save(exporter);
        campfireCooking(Ingredient.of(ItemRegistry.PERFECT_MARSHMALLOW_PILE), RecipeCategory.BUILDING_BLOCKS, ItemRegistry.BURNT_MARSHMALLOW_PILE, 0.1f, 100)
                .group("roasted:marshmallow_pile")
                .unlockedBy("has_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.MARSHMALLOW))
                .save(exporter);
        campfireCooking(Ingredient.of(ItemRegistry.BURNT_MARSHMALLOW_PILE), RecipeCategory.BUILDING_BLOCKS, ItemRegistry.CHARRED_MARSHMALLOW_PILE, 0.1f, 100)
                .group("roasted:marshmallow_pile")
                .unlockedBy("has_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.MARSHMALLOW))
                .save(exporter);
    }

    private void smoreRecipe(ItemLike result, ItemLike marshmallowStick, int minRoast, int maxRoast, RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, result)
                .pattern("C")
                .pattern("M")
                .pattern("C")
                .define('C', Items.COOKIE)
                .define('M', RoastedIngredient.createVanilla(marshmallowStick, minRoast, maxRoast))
                .group("roasted:smores")
                .unlockedBy("has_marshmallow_on_a_stick", minRoastTrigger(marshmallowStick, minRoast))
                .save(exporter, BuiltInRegistries.ITEM.getKey(result.asItem()) + "_from_" + BuiltInRegistries.ITEM.getKey(marshmallowStick.asItem()).getPath());
    }

    private Criterion<InventoryChangeTrigger.TriggerInstance> minRoastTrigger(ItemLike item, int min) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(item).hasComponents(DataComponentPredicate.builder().expect(DataComponentRegistry.ROASTEDNESS, min).build()).build());
    }
}
