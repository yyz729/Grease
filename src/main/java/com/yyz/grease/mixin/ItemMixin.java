package com.yyz.grease.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "inventoryTick", at = @At("RETURN"))
    private void injectInventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt != null && nbt.getLong("grease_age") - world.getTime() <= 0.0f) {
            nbt.remove("grease");
            nbt.remove("grease_age");
        }
    }
}
