package com.coda.roasted.datagen;

import com.coda.roasted.Roasted;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class TagProvider extends FabricTagProvider.BlockTagProvider {


    public TagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        tag(Roasted.SOUL_CAMPFIRES).add(BuiltInRegistries.BLOCK.getResourceKey(Blocks.SOUL_CAMPFIRE).get());
    }
}
