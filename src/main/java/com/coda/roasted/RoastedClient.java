package com.coda.roasted;

import com.coda.roasted.item.MarshmallowOnAStickItem;
import com.coda.roasted.registry.ItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class RoastedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceLocation roastedness = new ResourceLocation(Roasted.MODID, "roastedness");
        ClampedItemPropertyFunction function = (itemStack, clientLevel, livingEntity, i) -> itemStack.hasTag() ? itemStack.getTag().getInt(MarshmallowOnAStickItem.ROASTEDNESS_TAG) / 50f : 0;
        ItemProperties.register(ItemRegistry.MARSHMALLOW_STICK, roastedness, function);
        ItemProperties.register(ItemRegistry.SOUL_MARSHMALLOW_STICK, roastedness, function);
    }
}
