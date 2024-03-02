package com.coda.roasted.item.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;

public class RoastInfo {
    public static final Codec<RoastInfo> CODEC = Entry.CODEC.listOf().xmap(
            entries -> {
                ImmutableRangeMap.Builder<Integer, Info> builder = ImmutableRangeMap.builder();
                for (int i = 0; i < entries.size() - 1; i++) {
                    Entry lower = entries.get(i);
                    Entry upper = entries.get(i+1);
                    builder.put(Range.closedOpen(lower.lowerBound(), upper.lowerBound()), lower.info());
                }
                Entry last = entries.get(entries.size()-1);
                builder.put(Range.atLeast(last.lowerBound()), last.info());
                return new RoastInfo(builder.build());
            },
            rangeMap -> {
                ImmutableList.Builder<Entry> builder = ImmutableList.builder();
                for (Map.Entry<Range<Integer>, Info> rangeEntry : rangeMap.ranges.asMapOfRanges().entrySet()) {
                    var range = rangeEntry.getKey();
                    var info = rangeEntry.getValue();
                    builder.add(new Entry(range.lowerEndpoint(), info));
                }
                return builder.build();
            }
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RoastInfo> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    private int lastValue;
    private Info lastInfo;
    private boolean hasReachedEndpoint;
    private final ImmutableRangeMap<Integer, Info> ranges;

    public RoastInfo(ImmutableRangeMap<Integer, Info> ranges) {
        this.ranges = ranges;
    }

    public boolean hasReachedEndpoint(int value) {
        return hasReachedEndpoint || (hasReachedEndpoint = !ranges.getEntry(value).getKey().hasUpperBound());
    }

    public Info getInfo(int value) {
        if (value == lastValue && lastInfo != null) {
            return lastInfo;
        }

        lastValue = value;
        lastInfo = ranges.get(value);
        return lastInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ImmutableRangeMap.Builder<Integer, Info> ranges;
        private Entry last;

        private Builder() {
            ranges = new ImmutableRangeMap.Builder<>();
        }

        public Builder addRange(int lowerBound, int effectAmplifier, int effectDuration, String translationKey) {
            Entry current = new Entry(lowerBound, new Info(effectAmplifier, effectDuration, translationKey));
            if (last != null) {
                ranges.put(Range.closedOpen(last.lowerBound(), lowerBound), last.info());
            }
            last = current;
            return this;
        }

        public RoastInfo build() {
            ranges.put(Range.atLeast(last.lowerBound()), last.info());
            return new RoastInfo(ranges.build());
        }
    }

    public record Entry(int lowerBound, Info info) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("value").forGetter(Entry::lowerBound),
                        Codec.INT.fieldOf("amplifier").forGetter(entry -> entry.info().amplifier()),
                        Codec.INT.fieldOf("duration").forGetter(entry -> entry.info().duration()),
                        Codec.STRING.fieldOf("translation_key").forGetter(entry -> entry.info().translationKey())
                ).apply(instance, Entry::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Entry::lowerBound, ByteBufCodecs.VAR_INT, entry -> entry.info().amplifier(), ByteBufCodecs.VAR_INT, entry -> entry.info().duration(), ByteBufCodecs.STRING_UTF8, entry -> entry.info().translationKey(), Entry::new);

        private Entry(int lowerBound, int amplifier, int duration, String translationKey) {
            this(lowerBound, new Info(amplifier, duration, translationKey));
        }
    }

    public record Info(int amplifier, int duration, String translationKey) {}
}
