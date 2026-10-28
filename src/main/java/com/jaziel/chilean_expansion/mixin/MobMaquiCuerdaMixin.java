package com.jaziel.chilean_expansion.mixin;

import com.jaziel.chilean_expansion.item.ModItems;
import com.jaziel.chilean_expansion.util.MaquiCuerdaUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public abstract class MobMaquiCuerdaMixin extends LivingEntity implements MaquiCuerdaUtil {

    @Shadow @Nullable private Entity leashHolder;
    @Shadow public abstract void dropLeash(boolean broadcastPacket, boolean dropLead);

    @Unique
    private static final EntityDataAccessor<Boolean> DATA_MAQUI_LEASHED =
            SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BOOLEAN);

    protected MobMaquiCuerdaMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void chilean_expansion$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_MAQUI_LEASHED, false);
    }

    @Override
    public boolean chilean_expansion$isMaquiLeashed() {
        return this.entityData.get(DATA_MAQUI_LEASHED);
    }

    @Override
    public void chilean_expansion$setMaquiLeashed(boolean value) {
        this.entityData.set(DATA_MAQUI_LEASHED, value);
    }

    // Guardar en NBT para que no se pierda al salir del mundo
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void chilean_expansion$saveMaquiLeash(CompoundTag compound, CallbackInfo ci) {
        if (this.chilean_expansion$isMaquiLeashed()) {
            compound.putBoolean("MaquiLeashed", true);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void chilean_expansion$readMaquiLeash(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("MaquiLeashed")) {
            this.chilean_expansion$setMaquiLeashed(compound.getBoolean("MaquiLeashed"));
        }
    }

    // Reemplazar Items.LEAD por MAQUI_CUERDA cuando se suelta o se rompe la cuerda
    @ModifyArg(
            method = "dropLeash",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"),
            index = 0
    )
    private ItemLike chilean_expansion$replaceDroppedLead(ItemLike originalItem) {
        if (this.chilean_expansion$isMaquiLeashed()) {
            return ModItems.MAQUI_CUERDA.get();
        }
        return originalItem;
    }

    // Limpiar el estado después de soltar la cuerda
    @Inject(method = "dropLeash", at = @At("TAIL"))
    private void chilean_expansion$clearMaquiStateOnDrop(boolean broadcastPacket, boolean dropLead, CallbackInfo ci) {
        this.chilean_expansion$setMaquiLeashed(false);
    }

    // Controlar la física de resistencia y elasticidad en cada tick
    @Inject(method = "tickLeash", at = @At("HEAD"), cancellable = true)
    private void chilean_expansion$customMaquiLeashPhysics(CallbackInfo ci) {
        if (!this.chilean_expansion$isMaquiLeashed() || this.leashHolder == null) {
            return; // Si es una rienda vanilla, deja que Minecraft ejecute su código normal
        }

        Mob self = (Mob) (Object) this;
        if (!self.isAlive() || !this.leashHolder.isAlive()) {
            this.dropLeash(true, true);
            ci.cancel();
            return;
        }

        float distance = self.distanceTo(this.leashHolder);

        // 1. MÁS RESISTENTE: Se rompe a los 16 bloques en lugar de 10
        if (distance > 16.0F) {
            this.dropLeash(true, true);
            ci.cancel();
            return;
        }

        // 2. MENOS ELÁSTICA: Empieza a traccionar con firmeza desde los 4.5 bloques (vanilla es 6.0)
        if (distance > 4.5F) {
            double dx = (this.leashHolder.getX() - self.getX()) / (double) distance;
            double dy = (this.leashHolder.getY() - self.getY()) / (double) distance;
            double dz = (this.leashHolder.getZ() - self.getZ()) / (double) distance;

            // Multiplicador 0.65D (más rígido que el 0.4D vanilla) para evitar el efecto resorte
            self.setDeltaMovement(self.getDeltaMovement().add(
                    Math.copySign(dx * dx * 0.65D, dx),
                    Math.copySign(dy * dy * 0.65D, dy),
                    Math.copySign(dz * dz * 0.65D, dz)
            ));
            self.checkSlowFallDistance();
        } else if (self instanceof PathfinderMob pathfinderMob && distance > 2.5F) {
            // Hace que el mob camine hacia el titular antes de tensar la cuerda al máximo
            pathfinderMob.getNavigation().moveTo(this.leashHolder, 1.0D);
        }

        ci.cancel();
    }
}
