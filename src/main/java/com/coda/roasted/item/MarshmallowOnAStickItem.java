package com.coda.roasted.item;

import com.coda.roasted.registry.ItemRegistry;
import com.coda.roasted.recipe.Roaster;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MarshmallowOnAStickItem extends Item {

    public static final Map<Block, Item> CAMPFIRE_MUTATE = Maps.newHashMap();
    public static final String ROASTEDNESS_TAG = "Roastedness";
    final Roaster validRoastBlocks;

    public MarshmallowOnAStickItem(Properties properties, Roaster validRoasters) {
        super(properties);
        this.validRoastBlocks = validRoasters;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 25;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        CompoundTag tag = stack.getOrCreateTag();
        int roastedness = tag.getInt(ROASTEDNESS_TAG);
        if (roastedness < 5) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.unroasted").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
        } else if (roastedness < 15) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.warm").withStyle(Style.EMPTY.withColor(0xBD9479)).withStyle(ChatFormatting.ITALIC));
        } else if (roastedness < 20) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.perfect").withStyle(Style.EMPTY.withColor(0x935E3C)).withStyle(ChatFormatting.ITALIC));
        } else if (roastedness < 30) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.burnt").withStyle(Style.EMPTY.withColor(0x574134)).withStyle(ChatFormatting.ITALIC));
        } else if (roastedness <= 50) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.charred").withStyle(Style.EMPTY.withColor(0x454545)).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        EffectInfo effectInfo = getEffectInfo(stack);
        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, effectInfo.duration, effectInfo.amplifier));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, effectInfo.duration, Math.max(effectInfo.amplifier - 1, 0)));
        return livingEntity instanceof Player player && !player.getAbilities().instabuild ? getCraftingRemainingItem().getDefaultInstance().copy() : super.finishUsingItem(stack, level, livingEntity);
    }

    protected EffectInfo getEffectInfo(ItemStack stack) {
        int roastedness = stack.getOrCreateTag().getInt(ROASTEDNESS_TAG);
        // unroasted
        if (roastedness < 5) {
            return new EffectInfo(0, 100 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Warm
        else if (roastedness >= 5 && roastedness < 15) {
            return new EffectInfo(1, 200 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Perfect
        else if (roastedness >= 15 && roastedness < 20) {
            return new EffectInfo(2, 400  * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Burnt
        else if (roastedness >= 20 && roastedness < 30) {
            return new EffectInfo(1, 120 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Charred
        else {
            return new EffectInfo(0, 160 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!isSelected)
            return;

        if (entity instanceof Player player) {
            BlockHitResult result = fromPOVWithRange(level, player, 2);
            CompoundTag tag = stack.getOrCreateTag();
            int roastedness = tag.getInt(ROASTEDNESS_TAG);

            if (roastedness < 50 && level.random.nextFloat() > 0.75 && !result.getType().equals(HitResult.Type.MISS)) {
                BlockState blockState = level.getBlockState(result.getBlockPos());
                if (validRoastBlocks.isValid(blockState)) {
                    tag.putInt(ROASTEDNESS_TAG, roastedness+1);
                }
                if (CAMPFIRE_MUTATE.containsKey(blockState.getBlock())) {
                    ItemStack mutated = new ItemStack(CAMPFIRE_MUTATE.get(blockState.getBlock()));
                    mutated.setTag(tag);
                    player.getSlot(slotId).set(mutated);
                }
            }
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    BlockHitResult fromPOVWithRange(Level level, Player player, double range) {
        float yaw = player.getXRot();
        float pitch = player.getYRot();
        Vec3 eyePosition = player.getEyePosition();
        float h = Mth.cos(-pitch * ((float)Math.PI / 180) - (float)Math.PI);
        float i = Mth.sin(-pitch * ((float)Math.PI / 180) - (float)Math.PI);
        float j = -Mth.cos(-yaw * ((float)Math.PI / 180));
        float k = Mth.sin(-yaw * ((float)Math.PI / 180));
        float xDir = i * j;
        float yDir = k;
        float zDir = h * j;
        Vec3 lookedPosition = eyePosition.add((double)xDir * range, (double)yDir * range, (double)zDir * range);
        return level.clip(new ClipContext(eyePosition, lookedPosition, ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));
    }

    record EffectInfo(int amplifier, int duration) {};
}
