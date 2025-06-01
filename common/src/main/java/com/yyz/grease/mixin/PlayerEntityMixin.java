package com.yyz.grease.mixin;

import com.yyz.grease.Grease;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    PlayerEntity player = (PlayerEntity) (Object) this;

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",ordinal = 0))
    private boolean redirectLivingEntityDamage(LivingEntity instance, DamageSource source, float amount) {
        if(player.getMainHandStack().hasNbt() && player.getMainHandStack().getNbt().getString("grease").equals("magic") && player.getRandom().nextInt(100) < Grease.getConfig().magic_change){
            source =  player.getDamageSources().magic();
        }

        if(player.getMainHandStack().hasNbt() && player.getMainHandStack().getNbt().getString("grease").equals("sacred")){
            amount += Grease.getConfig().sacred_damage;
        }

        return instance.damage(source, amount);
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",ordinal = 0))
    private boolean redirectEntityDamage(Entity instance, DamageSource source, float amount) {
        if(player.getMainHandStack().hasNbt() && player.getMainHandStack().getNbt().getString("grease").equals("magic") && player.getRandom().nextInt(100) < Grease.getConfig().magic_change){
            source =  player.getDamageSources().magic();
        }

        if(player.getMainHandStack().hasNbt() && player.getMainHandStack().getNbt().getString("grease").equals("sacred") && instance instanceof LivingEntity entity1 && entity1.getGroup().equals(EntityGroup.UNDEAD)){
            amount += Grease.getConfig().sacred_damage;
        }

        return instance.damage(source, amount);
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void injectAttack(Entity entity, CallbackInfo ci) {
        World world = player.getWorld();
        if (!world.isClient) {

            ItemStack stack = player.getMainHandStack();
            NbtCompound nbt = stack.getNbt();
            if (nbt != null && entity != null && nbt.contains("grease") && nbt.getLong("grease_age") - world.getTime() > 0.0f) {
                switch (nbt.getString("grease")) {
                    case "lightning": {
                        if(!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                            player.getItemCooldownManager().set(stack.getItem(),Grease.getConfig().lightning_cooldown);
                            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                            lightning.setPosition(entity.getPos());
                            world.spawnEntity(lightning);
                        }
                        return;
                    }
                    case "fire": {
                        entity.setFireTicks(Grease.getConfig().fire_tick);
                        return;
                    }
                    case "poison": {
                        if(entity instanceof LivingEntity entity1){
                            entity1.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,Grease.getConfig().poison_tick));
                        }
                        return;
                    }
                    case "freezing": {
                        entity.setFrozenTicks(Grease.getConfig().freezing_tick * 2);
                        return;
                    }

                }
            }

        }
    }

}
