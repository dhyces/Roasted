package coda.roasted.crafting;

import coda.roasted.Roasted;
import coda.roasted.items.SmoreItem;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class SmoreRecipe extends ShapedRecipe {

    public SmoreRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, group, width, height, ingredients, result);
    }

    private static SmoreRecipe wrap(ShapedRecipe recipe, JsonObject json) {
        ItemStack stack = recipe.getResultItem();
        apply(recipe, stack, json);
        return new SmoreRecipe(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), stack);
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Serializer.SERIALIZER.get();
    }

    public static void apply(ShapedRecipe recipe, ItemStack stack, @Nullable JsonObject json) {
        if (json != null) {
            int roastedness = GsonHelper.getAsInt(GsonHelper.getAsJsonObject(json, "result"), "roastedness");
            if (roastedness > 0)  {

                NonNullList<Ingredient> ingredients = recipe.getIngredients();

                CompoundTag tag = stack.getOrCreateTag();
                tag.putInt(SmoreItem.ROASTEDNESS_NBT, roastedness);
                stack.setTag(tag);
            }
        }
    }

    public static class Serializer extends ShapedRecipe.Serializer {
        public static final RegistryObject<RecipeSerializer<SmoreRecipe>> SERIALIZER = RegistryObject.of(new ResourceLocation(Roasted.MOD_ID, "crafting_smores"), ForgeRegistries.RECIPE_SERIALIZERS);

        public Serializer() {
            setRegistryName(SERIALIZER.getId());
        }

        @Override
        public SmoreRecipe fromJson(ResourceLocation id, JsonObject json) {
            return wrap(super.fromJson(id, json), json);
        }

        @Override
        public ShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return wrap(super.fromNetwork(id, buffer), null);
        }
    }
}