package coda.roasted.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SmoreItem extends Item {
    public static final String ROASTEDNESS_NBT = "Roastedness";
    private final int duration;
    private final int amplifier;

    public SmoreItem(int amplifier, int duration, Properties builder) {
        super(builder);
        this.amplifier = amplifier;
        this.duration = duration;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, duration, Math.max(amplifier - 1, 0)));

        return super.finishUsingItem(stack, level, entity);
    }
}
