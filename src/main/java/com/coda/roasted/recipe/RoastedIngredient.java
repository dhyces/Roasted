package com.coda.roasted.recipe;

import com.coda.roasted.Roasted;
import com.coda.roasted.item.MarshmallowOnAStickItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class RoastedIngredient extends Ingredient {

    final ItemStack item;
    final int minRoast;
    final int maxRoast;

    public RoastedIngredient(ItemStack stack, int minRoast, int maxRoast) {
        super(Stream.of(new ItemValue(stack)));
        this.item = stack;
        this.minRoast = minRoast;
        this.maxRoast = maxRoast;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null) return false;
        return stack.is(item.getItem()) && isValidRoastedness(stack);
    }

    private boolean isValidRoastedness(ItemStack stack) {
        if (!stack.hasTag())
            return true;
        int roastedness = stack.getTag().getInt(MarshmallowOnAStickItem.ROASTEDNESS_TAG);
        return roastedness >= minRoast && roastedness < maxRoast;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("type", new JsonPrimitive("roasted:roasted"));
        jsonObject.add("item", new JsonPrimitive(Registry.ITEM.getId(item.getItem())));
        jsonObject.add("min_roast", new JsonPrimitive(minRoast));
        jsonObject.add("max_roast", new JsonPrimitive(maxRoast));
        return super.toJson();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(-1);
        buffer.writeResourceLocation(new ResourceLocation(Roasted.MODID, "roasted"));
        buffer.writeItem(item);
        buffer.writeVarInt(minRoast);
        buffer.writeVarInt(maxRoast);
    }
}
