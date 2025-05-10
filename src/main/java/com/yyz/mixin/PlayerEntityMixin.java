package com.yyz.mixin;

import com.yyz.Grease;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    PlayerEntity player = (PlayerEntity) (Object) this;

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",ordinal = 0))
    private boolean redirectLivingEntityDamage(LivingEntity instance, DamageSource source, float amount) {
        if(player.getMainHandStack().contains(Grease.GREASE_COMPONENT_TYPE) && player.getMainHandStack().getOrDefault(Grease.GREASE_COMPONENT_TYPE,"default").equals("magic") && player.getRandom().nextInt(100) < Grease.getConfig().magic_change){
            source =  player.getDamageSources().magic();
        }

        if(player.getMainHandStack().contains(Grease.GREASE_COMPONENT_TYPE) && player.getMainHandStack().getOrDefault(Grease.GREASE_COMPONENT_TYPE,"default").equals("sacred")){
            amount += Grease.getConfig().sacred_damage;
        }

        return instance.damage(source, amount);
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",ordinal = 0))
    private boolean redirectEntityDamage(Entity instance, DamageSource source, float amount) {
        if(player.getMainHandStack().contains(Grease.GREASE_COMPONENT_TYPE) && player.getMainHandStack().getOrDefault(Grease.GREASE_COMPONENT_TYPE,"default").equals("magic") && player.getRandom().nextInt(100) < Grease.getConfig().magic_change){
            source =  player.getDamageSources().magic();
        }

        if(player.getMainHandStack().contains(Grease.GREASE_COMPONENT_TYPE) && player.getMainHandStack().getOrDefault(Grease.GREASE_COMPONENT_TYPE,"default").equals("sacred")  && instance.getType().isIn(EntityTypeTags.UNDEAD)){
            amount += Grease.getConfig().sacred_damage;
        }

        return instance.damage(source, amount);
    }

}
