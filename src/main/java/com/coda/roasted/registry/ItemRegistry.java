package com.coda.roasted.registry;

import com.coda.roasted.Roasted;
import com.coda.roasted.item.MarshmallowOnAStickItem;
import com.coda.roasted.item.components.RoastInfo;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.UnaryOperator;

public class ItemRegistry {
    public static void init() {}

    public static final CreativeModeTab GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Roasted.id("roasted"), FabricItemGroup.builder().title(Component.translatable("itemGroup.roasted.roasted")).icon(() -> new ItemStack(ItemRegistry.MARSHMALLOW_STICK)).displayItems(ItemRegistry::addToTab).build());

    public static final Item MARSHMALLOW = register("marshmallow", new Item(new Item.Properties().food(new FoodProperties.Builder().fast().nutrition(2).saturationMod(0.05F).build())));
    public static final Item MARSHMALLOW_STICK = register("marshmallow_on_a_stick", makeMarshmallowOnAStick(3, .15F, Blocks.CAMPFIRE, builder ->
            builder.addRange(0, 0, 100, "roasted.roastedness.unroasted")
                    .addRange(5, 1, 200, "roasted.roastedness.warm")
                    .addRange(15, 2, 400, "roasted.roastedness.perfect")
                    .addRange(20, 1, 120, "roasted.roastedness.burnt")
                    .addRange(30, 0, 80, "roasted.roastedness.charred")
    ));
    public static final Item SOUL_MARSHMALLOW_STICK = register("soul_marshmallow_on_a_stick", makeMarshmallowOnAStick(5, .35F, Blocks.SOUL_CAMPFIRE, builder ->
            builder.addRange(0, 0, 200, "roasted.roastedness.unroasted")
                    .addRange(5, 1, 400, "roasted.roastedness.warm")
                    .addRange(15, 2, 800, "roasted.roastedness.perfect")
                    .addRange(20, 1, 240, "roasted.roastedness.burnt")
                    .addRange(30, 0, 160, "roasted.roastedness.charred")
    ));

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

    public static void addToTab(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(MARSHMALLOW);

        output.accept(MARSHMALLOW_STICK);
        output.accept(SOUL_MARSHMALLOW_STICK);

        output.accept(UNROASTED_SMORE);
        output.accept(WARM_SMORE);
        output.accept(PERFECT_SMORE);
        output.accept(BURNT_SMORE);
        output.accept(CHARRED_SMORE);

        output.accept(MARSHMALLOW_PILE);
        output.accept(WARM_MARSHMALLOW_PILE);
        output.accept(PERFECT_MARSHMALLOW_PILE);
        output.accept(BURNT_MARSHMALLOW_PILE);
        output.accept(CHARRED_MARSHMALLOW_PILE);
    }

    static Item makeSmore(int amplifier, int duration) {
        var movementSpeed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier);
        var digSpeed = new MobEffectInstance(MobEffects.DIG_SPEED, duration, Math.max(amplifier - 1, 0));
        var foodProperties = new FoodProperties.Builder().alwaysEat().fast();
        foodProperties.nutrition(7).saturationMod(0.5F);
        foodProperties.effect(movementSpeed, 1).effect(digSpeed, 1);
        return new Item(new Item.Properties().food(foodProperties.build()));
    }

    static Item makeMarshmallowOnAStick(int nutrition, float saturation, Block validRoastBlock, UnaryOperator<RoastInfo.Builder> roastednessBuilder) {
        var foodProperties = new FoodProperties.Builder().alwaysEat().fast();
        foodProperties.nutrition(nutrition).saturationMod(saturation);
        return new MarshmallowOnAStickItem(
                new Item.Properties()
                        .food(foodProperties.build())
                        .stacksTo(1)
                        .craftRemainder(Items.STICK)
                        .component(
                                DataComponentRegistry.ROAST_INFO,
                                roastednessBuilder.apply(RoastInfo.builder()).build()
                        )
                        .component(DataComponentRegistry.ROASTEDNESS, 0)
                        .component(DataComponentRegistry.ROASTER, validRoastBlock)
        );
    }

    static <T extends Item> T register(String id, T item) {
        return Registry.register(BuiltInRegistries.ITEM, Roasted.id(id), item);
    }
}
