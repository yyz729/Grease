package com.yyz.event;

import com.yyz.Grease;
import com.yyz.item.GreaseItem;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public class ModEvents {
    public static void registerClient(){
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (stack.hasNbt() && stack.getNbt().contains("grease")) {
                lines.add(1, Text.translatable("item.grease." + stack.getNbt().getString("grease") + "_grease").formatted(Formatting.GRAY));

            }
        });
    }


    public static void registerServer(){
        AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (!world.isClient) {

                ItemStack stack = playerEntity.getMainHandStack();
                NbtCompound nbt = stack.getNbt();
                if (nbt != null && entity != null && nbt.contains("grease") && nbt.getLong("grease_age") - world.getTime() > 0.0f) {
                    switch (nbt.getString("grease")) {
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
