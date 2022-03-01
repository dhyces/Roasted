package coda.roasted.registry;

import coda.roasted.Roasted;
import coda.roasted.items.MarshmallowOnAStickItem;
import coda.roasted.items.SmoreItem;
import coda.roasted.items.SoulMarshmallowOnAStickItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RoastedItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Roasted.MOD_ID);

    public static final RegistryObject<Item> MARSHMALLOW = ITEMS.register("marshmallow", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().fast().nutrition(2).saturationMod(0.05F).build())));
    public static final RegistryObject<Item> MARSHMALLOW_ON_A_STICK = ITEMS.register("marshmallow_on_a_stick", () -> new MarshmallowOnAStickItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().alwaysEat().fast().nutrition(3).saturationMod(0.15F).build())));
    public static final RegistryObject<Item> SOUL_MARSHMALLOW_ON_A_STICK = ITEMS.register("soul_marshmallow_on_a_stick", () -> new SoulMarshmallowOnAStickItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().alwaysEat().fast().nutrition(5).saturationMod(0.35F).build())));

    public static final RegistryObject<Item> SMORE_UNROASTED = ITEMS.register("unroasted_smore", () -> new SmoreItem(0, 100, new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().alwaysEat().fast().nutrition(7).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> SMORE_WARM = ITEMS.register("warm_smore", () -> new SmoreItem(1, 200, new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().alwaysEat().fast().nutrition(7).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> SMORE_PERFECT = ITEMS.register("perfect_smore", () -> new SmoreItem(2, 400, new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().alwaysEat().fast().nutrition(7).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> SMORE_BURNT = ITEMS.register("burnt_smore", () -> new SmoreItem(1, 120, new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().alwaysEat().fast().nutrition(7).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> SMORE_CHARRED = ITEMS.register("charred_smore", () -> new SmoreItem(0, 80, new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().alwaysEat().fast().nutrition(7).saturationMod(0.5F).build())));

}
