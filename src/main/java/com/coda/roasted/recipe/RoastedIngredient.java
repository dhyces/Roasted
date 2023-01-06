package com.coda.roasted.recipe;

import com.coda.roasted.Roasted;
import com.coda.roasted.item.MarshmallowOnAStickItem;
import com.coda.roasted.registry.ItemRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record RoastedIngredient(ItemStack item, int minRoast, int maxRoast) implements CustomIngredient {
    public static final Codec<RoastedIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.either(
                            ItemStack.CODEC,
                            BuiltInRegistries.ITEM.byNameCodec()
                    ).xmap(RoastedIngredient::eitherToStack, RoastedIngredient::stackToEither).fieldOf("item").forGetter(RoastedIngredient::item),
                    Codec.INT.fieldOf("min_roast").forGetter(RoastedIngredient::minRoast),
                    Codec.INT.fieldOf("max_roast").forGetter(RoastedIngredient::maxRoast)
            ).apply(instance, RoastedIngredient::new)
    );

    public static final Serializer SERIALIZER = new Serializer();

    private static ItemStack eitherToStack(Either<ItemStack, Item> either) {
        return either.left().isPresent() ? either.left().get() : new ItemStack(either.right().get());
    }

    private static Either<ItemStack, Item> stackToEither(ItemStack itemStack) {
        return itemStack.getCount() > 1 || itemStack.hasTag() ? Either.left(itemStack) : Either.right(itemStack.getItem());
    }

    public static Ingredient createVanilla(ItemStack item, int minRoast, int maxRoast) {
        return new RoastedIngredient(item, minRoast, maxRoast).toVanilla();
    }

    public static Ingredient createVanilla(ItemLike item, int minRoast, int maxRoast) {
        return new RoastedIngredient(new ItemStack(item), minRoast, maxRoast).toVanilla();
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null) return false;
        return stack.is(item.getItem()) && isValidRoastedness(stack);
    }

    private boolean isValidRoastedness(ItemStack stack) {
        if (!stack.hasTag())
            return minRoast == 0;
        int roastedness = stack.getTag().getInt(MarshmallowOnAStickItem.ROASTEDNESS_TAG);
        return roastedness >= minRoast && roastedness < maxRoast;
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        return List.of(new ItemStack(ItemRegistry.MARSHMALLOW_STICK));
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements CustomIngredientSerializer<RoastedIngredient> {
        public static final ResourceLocation ID = new ResourceLocation(Roasted.MODID, "roasted");

        @Override
        public ResourceLocation getIdentifier() {
            return ID;
        }

        @Override
        public RoastedIngredient read(JsonObject json) {
            return Util.getOrThrow(CODEC.parse(JsonOps.INSTANCE, json), JsonParseException::new);
        }

        @Override
        public void write(JsonObject json, RoastedIngredient ingredient) {
            if (CODEC.encodeStart(JsonOps.INSTANCE, ingredient).getOrThrow(false, Roasted.LOGGER::error) instanceof JsonObject obj) {
                for (Map.Entry<String, JsonElement> entry : obj.asMap().entrySet()) {
                    json.add(entry.getKey(), entry.getValue());
                }
            }
        }

        @Override
        public RoastedIngredient read(FriendlyByteBuf buf) {
            return new RoastedIngredient(buf.readItem(), buf.readInt(), buf.readInt());
        }

        @Override
        public void write(FriendlyByteBuf buffer, RoastedIngredient ingredient) {
            buffer.writeItem(ingredient.item);
            buffer.writeInt(ingredient.minRoast);
            buffer.writeInt(ingredient.maxRoast);
        }
    }
}
