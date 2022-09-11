package com.coda.roasted.recipe;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface Roaster {

    static Roaster of(Block block) {
        return blockState -> blockState.is(block);
    }

    static Roaster of(TagKey<Block> blockTagKey) {
        return blockState -> blockState.is(blockTagKey);
    }

    boolean isValid(BlockState blockState);
}
