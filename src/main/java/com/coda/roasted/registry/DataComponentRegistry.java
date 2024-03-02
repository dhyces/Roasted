package com.coda.roasted.registry;

import com.coda.roasted.Roasted;
import com.coda.roasted.item.components.RoastInfo;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.Block;

import java.util.function.UnaryOperator;

public class DataComponentRegistry {

    public static final DataComponentType<Integer> ROASTEDNESS = register("roastedness", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<RoastInfo> ROAST_INFO = register("roast_info", builder -> builder.persistent(RoastInfo.CODEC).networkSynchronized(RoastInfo.STREAM_CODEC));
    public static final DataComponentType<Block> ROASTER = register("roaster", builder -> builder.persistent(BuiltInRegistries.BLOCK.byNameCodec()));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Roasted.id(id), builder.apply(DataComponentType.builder()).build());
    }
}
