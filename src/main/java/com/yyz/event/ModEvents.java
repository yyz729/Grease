package com.yyz.event;

import com.yyz.Grease;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

public class ModEvents {
    public static void registerClient(){
        ItemTooltipCallback.EVENT.register((stack, context, type,lines) -> {
            if (stack.getComponents().contains(Grease.GREASE_COMPONENT_TYPE)) {

                lines.add(1, Text.translatable("item.grease." + stack.getOrDefault(Grease.GREASE_COMPONENT_TYPE,"default") + "_grease").formatted(Formatting.GRAY));

            }
        });
    }


    public static void registerServer(){
        AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (!world.isClient) {

                ItemStack stack = playerEntity.getMainHandStack();

                if (stack.contains(Grease.GREASE_COMPONENT_TYPE) && stack.getOrDefault(Grease.GREASE_AGE_COMPONENT_TYPE, 0L) - world.getTime() > 0.0f) {
                    String type = stack.getOrDefault(Grease.GREASE_COMPONENT_TYPE,"default");

                        switch (type) {
                            case "lightning": {
                                if(!playerEntity.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                                    playerEntity.getItemCooldownManager().set(stack.getItem(),Grease.getConfig().lightning_cooldown);
                                    LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                                    lightning.setPosition(entity.getPos());
                                    world.spawnEntity(lightning);
                                }
                                return ActionResult.PASS;
                            }
                            case "fire": {
                                entity.setFireTicks(Grease.getConfig().fire_tick);
                                return ActionResult.PASS;
                            }
                            case "poison": {
                                if(entity instanceof LivingEntity entity1){
                                    entity1.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,Grease.getConfig().poison_tick));
                                }
                                return ActionResult.PASS;
                            }
                            case "freezing": {
                                entity.setFrozenTicks(Grease.getConfig().freezing_tick * 2);
                                return ActionResult.PASS;
                            }

                        }

                }

            }
            return ActionResult.PASS;
        });

    }
}
