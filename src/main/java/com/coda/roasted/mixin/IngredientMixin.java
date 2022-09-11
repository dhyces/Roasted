package com.coda.roasted.mixin;

import com.coda.roasted.Roasted;
import com.coda.roasted.recipe.RoastedIngredient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(Ingredient.class)
public class IngredientMixin {

    @Inject(method = "fromJson", at = @At(value = "JUMP", opcode = Opcodes.IFEQ, ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private static void roasted_fromJson(@Nullable JsonElement json, CallbackInfoReturnable<Ingredient> cir) {
        if (json.isJsonObject() && isTypeValid(json.getAsJsonObject())) {
            JsonObject jsonObject = json.getAsJsonObject();
            ItemStack stack = new ItemStack(GsonHelper.getAsItem(jsonObject, "item"));
            int minRoast = jsonObject.getAsJsonPrimitive("roast_min").getAsInt();
            int maxRoast = jsonObject.getAsJsonPrimitive("roast_max").getAsInt();
            cir.setReturnValue(new RoastedIngredient(stack, minRoast, maxRoast));
        }
    }

    @Inject(method = "fromNetwork", at = @At("HEAD"), cancellable = true)
    private static void roasted_fromNetwork(FriendlyByteBuf buffer, CallbackInfoReturnable<Ingredient> cir) {
        int size = buffer.readVarInt();
        if (size == -1 && buffer.readResourceLocation().equals(new ResourceLocation(Roasted.MODID, "roasted"))) {
            cir.setReturnValue(new RoastedIngredient(buffer.readItem(), buffer.readVarInt(), buffer.readVarInt()));
        } else {
            cir.setReturnValue(Ingredient.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(size)));
        }
    }

    private static boolean isTypeValid(JsonObject json) {
        return json.has("type") &&
                json.get("type").isJsonPrimitive() &&
                json.getAsJsonPrimitive("type").isString() &&
                json.getAsJsonPrimitive("type").getAsString().equals("roasted:roasted");
    }
}
