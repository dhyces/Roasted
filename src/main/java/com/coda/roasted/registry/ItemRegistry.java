package com.coda.roasted.registry;

import com.coda.roasted.Roasted;
import com.coda.roasted.item.MarshmallowOnAStickItem;
import com.coda.roasted.recipe.Roaster;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;

public class ItemRegistry {

    public static void init() {}

    public static void registerCampfireMutations() {
        MarshmallowOnAStickItem.CAMPFIRE_MUTATE.put(Blocks.SOUL_CAMPFIRE, SOUL_MARSHMALLOW_STICK);
    }

    public static final CreativeModeTab GROUP = FabricItemGroupBuilder.build(new ResourceLocation(Roasted.MODID, "roasted"), () -> new ItemStack(ItemRegistry.MARSHMALLOW_STICK));

    public static final Item MARSHMALLOW = register("marshmallow", new Item(makeBasicFoodSettings(foodBuilder().fast().nutrition(2).saturationMod(0.05F).build())));
    public static final Item MARSHMALLOW_STICK = register("marshmallow_on_a_stick", makeMarshmallowOnAStick(3, .15F, Roaster.of(BlockTags.CAMPFIRES)));
    public static final Item SOUL_MARSHMALLOW_STICK = register("soul_marshmallow_on_a_stick", makeMarshmallowOnAStick(5, .35F, Roaster.of(Blocks.SOUL_CAMPFIRE)));

    public static final Item UNROASTED_SMORE = register("unroasted_smore", makeSmore(0, 100));
    public static final Item WARM_SMORE = register("warm_smore", makeSmore(1, 200));
    public static final Item PERFECT_SMORE = register("perfect_smore", makeSmore(2, 400));
    public static final Item BURNT_SMORE = register("burnt_smore", makeSmore(1, 120));
    public static final Item CHARRED_SMORE = register("charred_smore", makeSmore(0, 80));

    public static final BlockItem MARSHMALLOW_PILE = register("pile_of_marshmallows", new BlockItem(BlockRegistry.MARSHMALLOW_PILE, new Item.Properties().tab(GROUP)));
    public static final BlockItem WARM_MARSHMALLOW_PILE = register("pile_of_warm_marshmallows", new BlockItem(BlockRegistry.WARM_MARSHMALLOW_PILE, new Item.Properties().tab(GROUP)));
    public static final BlockItem PERFECT_MARSHMALLOW_PILE = register("pile_of_perfect_marshmallows", new BlockItem(BlockRegistry.PERFECT_MARSHMALLOW_PILE, new Item.Properties().tab(GROUP)));
    public static final BlockItem BURNT_MARSHMALLOW_PILE = register("pile_of_burnt_marshmallows", new BlockItem(BlockRegistry.BURNT_MARSHMALLOW_PILE, new Item.Properties().tab(GROUP)));
    public static final BlockItem CHARRED_MARSHMALLOW_PILE = register("pile_of_charred_marshmallows", new BlockItem(BlockRegistry.CHARRED_MARSHMALLOW_PILE, new Item.Properties().tab(GROUP)));

    static FoodProperties.Builder foodBuilder() {
        return new FoodProperties.Builder();
    }

    static Item.Properties makeBasicFoodSettings(FoodProperties foodProperties) {
        return new FabricItemSettings().tab(GROUP).food(foodProperties);
    }

    static Item makeSmore(int amplifier, int duration) {
        var movementSpeed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier);
        var digSpeed = new MobEffectInstance(MobEffects.DIG_SPEED, duration, Math.max(amplifier - 1, 0));
        var foodProperties = foodBuilder().alwaysEat().fast();
        foodProperties.nutrition(7).saturationMod(0.5F);
        foodProperties.effect(movementSpeed, 100).effect(digSpeed, 100);
        return new Item(makeBasicFoodSettings(foodProperties.build()));
    }

    static Item makeMarshmallowOnAStick(int nutrition, float saturation, Roaster validRoastBlocks) {
        var foodProperties = foodBuilder().alwaysEat().fast();
        foodProperties.nutrition(nutrition).saturationMod(saturation);
        return new MarshmallowOnAStickItem(makeBasicFoodSettings(foodProperties.build()).stacksTo(1).craftRemainder(Items.STICK), validRoastBlocks);
    }

    static <T extends Item> T register(String id, T item) {
        return Registry.register(Registry.ITEM, new ResourceLocation(Roasted.MODID, id), item);
    }
}
