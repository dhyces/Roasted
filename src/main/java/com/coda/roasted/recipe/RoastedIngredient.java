package com.coda.roasted.recipe;

import com.coda.roasted.Roasted;
import com.coda.roasted.registry.DataComponentRegistry;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public record RoastedIngredient(ItemStack item, int minRoast, int maxRoast) implements CustomIngredient {
    public static final Codec<RoastedIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.either(
                            ItemStack.SIMPLE_ITEM_CODEC,
                            BuiltInRegistries.ITEM.byNameCodec()
                    ).xmap(
                            either -> either.map(Function.identity(), ItemStack::new),
                            stack -> stack.has(DataComponentRegistry.ROASTEDNESS) ? Either.left(stack) : Either.right(stack.getItem())
                    ).fieldOf("item").forGetter(RoastedIngredient::item),
                    Codec.INT.fieldOf("min_roast").forGetter(RoastedIngredient::minRoast),
                    Codec.INT.fieldOf("max_roast").forGetter(RoastedIngredient::maxRoast)
            ).apply(instance, RoastedIngredient::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RoastedIngredient> STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC, RoastedIngredient::item, ByteBufCodecs.VAR_INT, RoastedIngredient::minRoast, ByteBufCodecs.VAR_INT, RoastedIngredient::maxRoast, RoastedIngredient::new);

    public static final Serializer SERIALIZER = new Serializer();

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
        if (!stack.has(DataComponentRegistry.ROASTEDNESS))
            return minRoast == 0;
        int roastedness = stack.get(DataComponentRegistry.ROASTEDNESS);
        return roastedness >= minRoast && roastedness < maxRoast;
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        var stack = item;
        if (minRoast != 0) {
            stack.set(DataComponentRegistry.ROASTEDNESS, minRoast);
        }
        return List.of(item);
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
        public static final ResourceLocation ID = Roasted.id("roasted");

        @Override
        public ResourceLocation getIdentifier() {
            return ID;
        }

        @Override
        public Codec<RoastedIngredient> getCodec(boolean allowEmpty) {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RoastedIngredient> getPacketCodec() {
            return STREAM_CODEC;
        }
    }
}
