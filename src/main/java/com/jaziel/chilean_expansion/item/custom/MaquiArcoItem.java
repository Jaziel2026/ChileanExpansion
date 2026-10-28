package com.jaziel.chilean_expansion.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MaquiArcoItem extends BowItem {
    public MaquiArcoItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            boolean hasInfiniteArrows = player.getAbilities().instabuild || stack.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS) > 0;
            ItemStack arrowStack = player.getProjectile(stack);

            if (!arrowStack.isEmpty() || hasInfiniteArrows) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                int chargeTime = this.getUseDuration(stack) - timeLeft;
                // Dividir por 30.0F hace que tarde un 50% más en cargar que el arco normal
                float power = (float)chargeTime / 30.0F;
                power = (power * power + power * 2.0F) / 3.0F;

                if (power > 1.0F) {
                    power = 1.0F;
                }

                if ((double)power >= 0.1D) {
                    if (!level.isClientSide) {
                        ArrowItem arrowItem = (ArrowItem)(arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
                        AbstractArrow arrowEntity = arrowItem.createArrow(level, arrowStack, player);

                        // Multiplicar el poder por 4.5F (vanilla es 3.0F) aumenta el alcance y la velocidad
                        arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 4.5F, 1.0F);

                        if (power == 1.0F) {
                            arrowEntity.setCritArrow(true);
                        }

                        // Añadir daño extra al proyectil
                        arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + 2.0D);

                        level.addFreshEntity(arrowEntity);
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
}