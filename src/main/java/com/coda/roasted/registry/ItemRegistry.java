package com.coda.roasted.registry;

import com.coda.roasted.Roasted;
import com.coda.roasted.item.MarshmallowOnAStickItem;
import com.coda.roasted.recipe.Roaster;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private static final Map<ResourceLocation, Item> ITEMS = new HashMap<>();

    public static void init() {}

    public static final CreativeModeTab GROUP = FabricItemGroup.builder(new ResourceLocation(Roasted.MODID, "roasted")).icon(() -> new ItemStack(ItemRegistry.MARSHMALLOW_STICK)).build();

    public static final Item MARSHMALLOW = register("marshmallow", new Item(makeBasicFoodSettings(foodBuilder().fast().nutrition(2).saturationMod(0.05F).build())));
    public static final Item MARSHMALLOW_STICK = register("marshmallow_on_a_stick", makeMarshmallowOnAStick(3, .15F, Roaster.of(BlockTags.CAMPFIRES)));
    public static final Item SOUL_MARSHMALLOW_STICK = register("soul_marshmallow_on_a_stick", makeMarshmallowOnAStick(5, .35F, Roaster.of(Blocks.SOUL_CAMPFIRE)));

    public static final Item UNROASTED_SMORE = register("unroasted_smore", makeSmore(0, 100));
    public static final Item WARM_SMORE = register("warm_smore", makeSmore(1, 200));
    public static final Item PERFECT_SMORE = register("perfect_smore", makeSmore(2, 400));
    public static final Item BURNT_SMORE = register("burnt_smore", makeSmore(1, 120));
    public static final Item CHARRED_SMORE = register("charred_smore", makeSmore(0, 80));

    public static final BlockItem MARSHMALLOW_PILE = register("pile_of_marshmallows", new BlockItem(BlockRegistry.MARSHMALLOW_PILE, new Item.Properties()));
    public static final BlockItem WARM_MARSHMALLOW_PILE = register("pile_of_warm_marshmallows", new BlockItem(BlockRegistry.WARM_MARSHMALLOW_PILE, new Item.Properties()));
    public static final BlockItem PERFECT_MARSHMALLOW_PILE = register("pile_of_perfect_marshmallows", new BlockItem(BlockRegistry.PERFECT_MARSHMALLOW_PILE, new Item.Properties()));
    public static final BlockItem BURNT_MARSHMALLOW_PILE = register("pile_of_burnt_marshmallows", new BlockItem(BlockRegistry.BURNT_MARSHMALLOW_PILE, new Item.Properties()));
    public static final BlockItem CHARRED_MARSHMALLOW_PILE = register("pile_of_charred_marshmallows", new BlockItem(BlockRegistry.CHARRED_MARSHMALLOW_PILE, new Item.Properties()));

    public static void addToTab(FabricItemGroupEntries entries) {
        entries.accept(MARSHMALLOW);

        entries.accept(MARSHMALLOW_STICK);
        entries.accept(SOUL_MARSHMALLOW_STICK);

        entries.accept(UNROASTED_SMORE);
        entries.accept(WARM_SMORE);
        entries.accept(PERFECT_SMORE);
        entries.accept(BURNT_SMORE);
        entries.accept(CHARRED_SMORE);
        
        entries.accept(MARSHMALLOW_PILE);
        entries.accept(WARM_MARSHMALLOW_PILE);
        entries.accept(PERFECT_MARSHMALLOW_PILE);
        entries.accept(BURNT_MARSHMALLOW_PILE);
        entries.accept(CHARRED_MARSHMALLOW_PILE);
    }

    static FoodProperties.Builder foodBuilder() {
        return new FoodProperties.Builder();
    }

    static Item.Properties makeBasicFoodSettings(FoodProperties foodProperties) {
        return new FabricItemSettings().food(foodProperties);
    }

    static Item makeSmore(int amplifier, int duration) {
        var movementSpeed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier);
        var digSpeed = new MobEffectInstance(MobEffects.DIG_SPEED, duration, Math.max(amplifier - 1, 0));
        var foodProperties = foodBuilder().alwaysEat().fast();
        foodProperties.nutrition(7).saturationMod(0.5F);
        foodProperties.effect(movementSpeed, 1).effect(digSpeed, 1);
        return new Item(makeBasicFoodSettings(foodProperties.build()));
    }

    static Item makeMarshmallowOnAStick(int nutrition, float saturation, Roaster validRoastBlocks) {
        var foodProperties = foodBuilder().alwaysEat().fast();
        foodProperties.nutrition(nutrition).saturationMod(saturation);
        return new MarshmallowOnAStickItem(makeBasicFoodSettings(foodProperties.build()).stacksTo(1).craftRemainder(Items.STICK), validRoastBlocks);
    }

    static <T extends Item> T register(String id, T item) {
        var key = new ResourceLocation(Roasted.MODID, id);
        var registered = Registry.register(BuiltInRegistries.ITEM, key, item);
        ITEMS.put(key, registered);
        return registered;
    }
}
