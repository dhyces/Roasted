package com.coda.roasted.item;

import com.coda.roasted.recipe.Roaster;
import com.coda.roasted.registry.ItemRegistry;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import java.util.Random;

public class MarshmallowOnAStickItem extends Item {

    public static final Map<Block, Item> CAMPFIRE_MUTATE = Maps.newHashMap();
    public static final String ROASTEDNESS_TAG = "Roastedness";
    protected final Roaster validRoastBlocks;

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
            tooltipComponents.add(Component.translatable("roasted.roastedness.unroasted").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else if (roastedness < 15) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.warm").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else if (roastedness < 20) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.perfect").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else if (roastedness < 30) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.burnt").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else if (roastedness <= 50) {
            tooltipComponents.add(Component.translatable("roasted.roastedness.charred").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        EffectInfo effectInfo = getEffectInfo(stack);
        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, effectInfo.duration, effectInfo.amplifier));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, effectInfo.duration, Math.max(effectInfo.amplifier - 1, 0)));
        return livingEntity instanceof Player player && !player.getAbilities().instabuild ? getCraftingRemainingItem().getDefaultInstance().copy() : super.finishUsingItem(stack, level, livingEntity);
    }

    //TODO: make more extensible
    protected EffectInfo getEffectInfo(ItemStack stack) {
        int roastedness = stack.getOrCreateTag().getInt(ROASTEDNESS_TAG);
        // Unroasted
        if (roastedness < 5) {
            return new EffectInfo(0, 100 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Warm
        else if (roastedness < 15) {
            return new EffectInfo(1, 200 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Perfect
        else if (roastedness < 20) {
            return new EffectInfo(2, 400 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Burnt
        else if (roastedness < 30) {
            return new EffectInfo(1, 120 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
        // Charred
        else {
            return new EffectInfo(0, 80 * (stack.is(ItemRegistry.SOUL_MARSHMALLOW_STICK) ? 2 : 1));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!isSelected)
            return;

        if (entity instanceof Player player) {
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            CompoundTag tag = stack.getOrCreateTag();
            int roastedness = tag.getInt(ROASTEDNESS_TAG);

            if (!level.isClientSide() && roastedness < 50 && level.random.nextFloat() > 0.75 && lookAt(player, result.getBlockPos(), 0.03, 2)) {
                BlockState blockState = level.getBlockState(result.getBlockPos());
                if (validRoastBlocks.isValid(blockState)) {
                    tag.putInt(ROASTEDNESS_TAG, roastedness + 1);
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

    private boolean lookAt(Player player, BlockPos pos, double radius, double range) {
        Vec3 viewVec = player.getViewVector(1.0F).normalize();
        Vec3 distVec = new Vec3((pos.getX() + 0.5) - player.getX(), (pos.getY() + 0.4375) - player.getEyeY(), (pos.getZ() + 0.5) - player.getZ());
        double distLen = distVec.length();
        distVec = distVec.normalize();
        double d1 = viewVec.dot(distVec);
        return d1 > 1.0D - radius / distLen && distLen <= range;
    }

    record EffectInfo(int amplifier, int duration) {}

}
