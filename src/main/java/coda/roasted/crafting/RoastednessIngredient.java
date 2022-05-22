package coda.roasted.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class RoastednessIngredient extends Ingredient {
    private static final String ROASTED_KEY = "Roastedness";

    private final ItemStack stack;
    private final int min;
    private final int max;

    protected RoastednessIngredient(ItemStack stack, int roastedMin, int roastedMax) {
        super(Stream.of(new Ingredient.ItemValue(stack)));
        this.stack = stack;
        this.min = roastedMin;
        this.max = roastedMax;
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null) return false;
        if (!input.is(stack.getItem())) return false;

        var roastedness = getRoastedness(input.getTag());
        if (roastedness == -1) return false;
        return roastedness >= min && roastedness < max;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public JsonElement toJson() {
        var json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(Serializer.INSTANCE).toString());
        json.addProperty("item", stack.getItem().getRegistryName().toString());
        json.addProperty("count", stack.getCount());
        json.addProperty("roast_min", min);
        json.addProperty("roast_max", max);
        return json;
    }

    public static int getRoastedness(@Nullable CompoundTag tag)
    {
        if (tag == null) return -1;
        if (!tag.contains(ROASTED_KEY)) return 0;
        return tag.getInt(ROASTED_KEY);
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    public static class Serializer implements IIngredientSerializer<RoastednessIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public RoastednessIngredient parse(FriendlyByteBuf buffer) {
            return new RoastednessIngredient(buffer.readItem(), buffer.readInt(), buffer.readInt());
        }

        @Override
        public RoastednessIngredient parse(JsonObject json) {
            var stack = CraftingHelper.getItemStack(json, false);
            return new RoastednessIngredient(stack, GsonHelper.getAsInt(json, "roast_min"), GsonHelper.getAsInt(json, "roast_max"));
        }

        @Override
        public void write(FriendlyByteBuf buffer, RoastednessIngredient ingredient) {
            buffer.writeItem(ingredient.stack);
            buffer.writeInt(ingredient.min);
            buffer.writeInt(ingredient.max);
        }
    }
}
