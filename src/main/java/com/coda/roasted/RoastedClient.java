package com.coda.roasted;

import com.coda.roasted.registry.DataComponentRegistry;
import com.coda.roasted.registry.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class RoastedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceLocation roastedness = new ResourceLocation(Roasted.MODID, "roastedness");
        ClampedItemPropertyFunction function = (itemStack, clientLevel, livingEntity, i) -> itemStack.get(DataComponentRegistry.ROASTEDNESS) / 50f;
        ItemProperties.register(ItemRegistry.MARSHMALLOW_STICK, roastedness, function);
        ItemProperties.register(ItemRegistry.SOUL_MARSHMALLOW_STICK, roastedness, function);
    }
}
