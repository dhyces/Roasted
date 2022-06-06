package coda.roasted.blocks;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class CharredMarshmallowBlock extends Block {

    public CharredMarshmallowBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public void stepOn(Level p_153777_, BlockPos p_153778_, BlockState p_153779_, Entity p_153780_) {
        if (!p_153780_.fireImmune() && p_153780_ instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)p_153780_)) {
            p_153780_.hurt(DamageSource.HOT_FLOOR, 1.0F);
        }
    }

    public void animateTick(BlockState p_54920_, Level p_54921_, BlockPos p_54922_, Random random) {
        if (random.nextFloat() > 0.5F) {
            double d0 = (double) p_54922_.getX() + 0.5;
            double d1 = (double) p_54922_.getY() + 0.5;
            double d2 = (double) p_54922_.getZ() + 0.5;
            double d3 = ((double) random.nextFloat() - 0.5D) * 0.25D;
            double d4 = ((double) random.nextFloat() - 0.5D) * 0.25D;
            double d5 = ((double) random.nextFloat() - 0.5D) * 0.25D;

            p_54921_.addParticle(ParticleTypes.SMALL_FLAME, d0, d1, d2, d3, d4, d5);

        }
    }

}
