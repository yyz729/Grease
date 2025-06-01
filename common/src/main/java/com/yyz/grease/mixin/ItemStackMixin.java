package com.yyz.grease.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    ItemStack stack = (ItemStack) (Object) this;
    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void injectGetTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        if (stack.hasNbt() && stack.getNbt().contains("grease")) {
            cir.getReturnValue().add(1, Text.translatable("item.grease." + stack.getNbt().getString("grease") + "_grease").formatted(Formatting.GRAY));

        }
    }
}
