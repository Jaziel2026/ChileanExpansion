package com.jaziel.chilean_expansion.item.custom;

import com.jaziel.chilean_expansion.util.MaquiCuerdaUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.gameevent.GameEvent;

public class MaquiCuerdaItem extends LeadItem {
    public MaquiCuerdaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Mob mob) {
            // Si ya está atado al jugador, lo desatamos
            if (mob.getLeashHolder() == player) {
                mob.dropLeash(true, !player.getAbilities().instabuild);
                mob.gameEvent(GameEvent.ENTITY_INTERACT, player);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }

            // Verificar si puede ser atado
            if (canMaquiLeash(mob, player)) {
                if (!player.level().isClientSide) {
                    ((MaquiCuerdaUtil) mob).chilean_expansion$setMaquiLeashed(true);
                    mob.setLeashedTo(player, true);
                }
                stack.shrink(1);
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    public static boolean canMaquiLeash(Mob mob, Player player) {
        if (mob.isLeashed() || mob.isRemoved()) return false;

        // Mobs adicionales permitidos por la cuerda de maqui
        if (mob instanceof Zombie || mob instanceof AbstractSkeleton || mob instanceof Creeper || mob instanceof Villager) {
            return true;
        }

        // Mobs que la cuerda vanilla ya puede atar
        return mob.canBeLeashed(player);
    }
}
