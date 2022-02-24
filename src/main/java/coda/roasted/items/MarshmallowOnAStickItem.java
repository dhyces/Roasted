package coda.roasted.items;

import coda.roasted.Roasted;
import coda.roasted.init.RoastedItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MarshmallowOnAStickItem extends Item {

    public MarshmallowOnAStickItem(Properties p_41383_) {
        super(p_41383_);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Roasted.CALLBACKS.add(() -> ItemProperties.register(this, new ResourceLocation(Roasted.MOD_ID, "roastedness"), (stack, world, player, i) -> stack.hasTag() ? stack.getTag().getInt("Roastedness") / 50f : 0)));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        int amplifier = 0;
        int duration = 100;
        int roastedness = stack.getTag().getInt("Roastedness");

        // Warm
        if (roastedness >= 5 && roastedness < 15) {
            amplifier = 1;
            duration = 200;
        }
        // Perfect
        else if (roastedness >= 15 && roastedness < 20) {
            amplifier = 2;
            duration = 400;
        }
        // Burnt
        else if (roastedness >= 20 && roastedness < 30) {
            amplifier = 1;
            duration = 120;
        }
        // Charred
        else if (roastedness >= 33 && roastedness < 50) {
            amplifier = 0; // technically not needed, but it is here so I don't go crazy :)
            duration = 80;
        }

        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, duration, Math.max(amplifier - 1, 0)));
        if (entity instanceof Player) {
            if (!((Player) entity).getAbilities().instabuild) {
                entity.setItemInHand(entity.getUsedItemHand(), new ItemStack(Items.STICK));
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 25;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getInt("Roastedness") < 5) {
            tooltip.add(new TranslatableComponent("roasted.roastedness.unroasted").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
        else if (tag.getInt("Roastedness") < 15) {
            tooltip.add(new TranslatableComponent("roasted.roastedness.warm").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
        else if (tag.getInt("Roastedness") < 20) {
            tooltip.add(new TranslatableComponent("roasted.roastedness.perfect").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
        else if (tag.getInt("Roastedness") < 30) {
            tooltip.add(new TranslatableComponent("roasted.roastedness.burnt").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
        else if (tag.getInt("Roastedness") <= 50) {
            tooltip.add(new TranslatableComponent("roasted.roastedness.charred").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
        super.inventoryTick(stack, level, entity, slot, held);
        BlockHitResult rt = getPlayerPOVHitResult(level, (Player) entity, ClipContext.Fluid.SOURCE_ONLY);
        CompoundTag tag = stack.getOrCreateTag();
        Random rand = new Random();

        if (tag.getInt("Roastedness") < 50 && rand.nextFloat() > 0.75F && held && lookAt((Player) entity, rt.getBlockPos(), 0.3D)) {
            if (level.getBlockState(rt.getBlockPos()).is(Blocks.CAMPFIRE)) {
                tag.putInt("Roastedness", tag.getInt("Roastedness") + 1);
                tag.putBoolean("Roasted", true);
            } else if (level.getBlockState(rt.getBlockPos()).is(Blocks.SOUL_CAMPFIRE)) {
                ItemStack newStack = new ItemStack(RoastedItems.SOUL_MARSHMALLOW_ON_A_STICK.get());

                entity.getSlot(slot).set(newStack);
                newStack.setTag(tag);
            }
        }
    }

    public static boolean lookAt(Player player, BlockPos pos, double range) {
        Vec3 vec3 = player.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(pos.getX() - player.getX(), pos.getY() - player.getEyeY(), pos.getZ() - player.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - range / d0;
    }
}
