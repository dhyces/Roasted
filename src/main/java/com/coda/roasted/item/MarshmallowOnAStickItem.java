package com.coda.roasted.item;

import com.coda.roasted.item.components.RoastInfo;
import com.coda.roasted.registry.DataComponentRegistry;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MarshmallowOnAStickItem extends Item {
    public static final Map<Block, Item> CAMPFIRE_MUTATE = new Reference2ObjectOpenHashMap<>();

    public MarshmallowOnAStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 25;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String translation = stack.get(DataComponentRegistry.ROAST_INFO).getInfo(stack.get(DataComponentRegistry.ROASTEDNESS)).translationKey();
        tooltipComponents.add(Component.translatable(translation).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        RoastInfo.Info effectInfo = stack.get(DataComponentRegistry.ROAST_INFO).getInfo(stack.get(DataComponentRegistry.ROASTEDNESS));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, effectInfo.duration(), effectInfo.amplifier()));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, effectInfo.duration(), Math.max(effectInfo.amplifier() - 1, 0)));
        return livingEntity instanceof Player player && !player.getAbilities().instabuild ? getCraftingRemainingItem().getDefaultInstance().copy() : super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!isSelected)
            return;

        if (entity instanceof Player player) {
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            int roastedness = stack.get(DataComponentRegistry.ROASTEDNESS);
            RoastInfo info = stack.get(DataComponentRegistry.ROAST_INFO);

            if (!level.isClientSide() && !info.hasReachedEndpoint(roastedness) && level.random.nextFloat() > 0.75 && lookAt(player, result.getBlockPos(), 0.03, 2)) {
                BlockState blockState = level.getBlockState(result.getBlockPos());
                if (blockState.is(stack.get(DataComponentRegistry.ROASTER))) {
                    stack.set(DataComponentRegistry.ROASTEDNESS, roastedness+1);
                }
                if (CAMPFIRE_MUTATE.containsKey(blockState.getBlock())) {
                    ItemStack mutated = new ItemStack(CAMPFIRE_MUTATE.get(blockState.getBlock()).builtInRegistryHolder(), 1, stack.getComponentsPatch());
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
}
