package com.yyz.mixin;

import com.yyz.Grease;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

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

}
