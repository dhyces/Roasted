package com.coda.roasted.registry;

import com.coda.roasted.Roasted;
import com.coda.roasted.block.CharredMarshmallowBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BlockRegistry {

    public static void init() {}

    public static final Block MARSHMALLOW_PILE = register("pile_of_marshmallows", new Block(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.FUNGUS).strength(1.0F)));
    public static final Block WARM_MARSHMALLOW_PILE = register("pile_of_warm_marshmallows", new Block(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.FUNGUS).strength(1.0F)));
    public static final Block PERFECT_MARSHMALLOW_PILE = register("pile_of_perfect_marshmallows", new Block(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.FUNGUS).strength(1.0F)));
    public static final Block BURNT_MARSHMALLOW_PILE = register("pile_of_burnt_marshmallows", new Block(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.FUNGUS).strength(1.0F)));
    public static final Block CHARRED_MARSHMALLOW_PILE = register("pile_of_charred_marshmallows", new CharredMarshmallowBlock(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.FUNGUS).strength(1.0F)));

    static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roasted.MODID, id), block);
    }
}
