package com.coda.roasted;

import com.coda.roasted.item.MarshmallowOnAStickItem;
import com.coda.roasted.recipe.RoastedIngredient;
import com.coda.roasted.registry.BlockRegistry;
import com.coda.roasted.registry.ItemRegistry;
import com.google.common.collect.Maps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Roasted implements ModInitializer {

    public static final String MODID = "roasted";
    public static final Logger LOGGER = LogManager.getLogger(Roasted.class);
    private static final Map<Block, Item> CAMPFIRE_MUTATE_PRE = Maps.newHashMap();

    @Override
    public void onInitialize() {
        BlockRegistry.init();
        ItemRegistry.init();
        registerCampfireMutations();
        ItemGroupEvents.modifyEntriesEvent(ItemRegistry.GROUP).register(ItemRegistry::addToTab);
        CustomIngredientSerializer.register(RoastedIngredient.SERIALIZER);
    }

    private void registerCampfireMutations() {
        MarshmallowOnAStickItem.CAMPFIRE_MUTATE.put(Blocks.SOUL_CAMPFIRE, ItemRegistry.SOUL_MARSHMALLOW_STICK);
        MarshmallowOnAStickItem.CAMPFIRE_MUTATE.putAll(CAMPFIRE_MUTATE_PRE);
    }

    public static Item registerCampfireMutation(Block block, Item item) {
        return CAMPFIRE_MUTATE_PRE.put(block, item);
    }

    public static final TagKey<Block> SOUL_CAMPFIRES = TagKey.create(Registries.BLOCK, new ResourceLocation(MODID, "soul_campfires"));
}
