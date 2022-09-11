package com.coda.roasted.datagen;

import com.coda.roasted.Roasted;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.world.level.block.Blocks;

public class TagDatagen extends FabricTagProvider.BlockTagProvider {

    public TagDatagen(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        tag(Roasted.SOUL_CAMPFIRES).add(Blocks.SOUL_CAMPFIRE);
    }
}
