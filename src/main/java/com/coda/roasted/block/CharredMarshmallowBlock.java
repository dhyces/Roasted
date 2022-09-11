package com.coda.roasted.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CharredMarshmallowBlock extends Block {

    public CharredMarshmallowBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (!entity.fireImmune() && entity instanceof LivingEntity living && !EnchantmentHelper.hasFrostWalker(living)) {
            living.hurt(DamageSource.HOT_FLOOR, 1.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextFloat() > 0.5F) {
            double x0 = (double) pos.getX() + 0.5;
            double y0 = (double) pos.getY() + 0.5;
            double z0 = (double) pos.getZ() + 0.5;
            double x = ((double) random.nextFloat() - 0.5D) * 0.25D;
            double y = ((double) random.nextFloat() - 0.5D) * 0.25D;
            double z = ((double) random.nextFloat() - 0.5D) * 0.25D;

            level.addParticle(ParticleTypes.SMALL_FLAME, x0, y0, z0, x, y, z);
        }
    }
}
