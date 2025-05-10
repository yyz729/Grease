package com.yyz.mixin;

import com.yyz.Grease;
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

        if (stack.contains(Grease.GREASE_AGE_COMPONENT_TYPE) && stack.getOrDefault(Grease.GREASE_AGE_COMPONENT_TYPE,0L) - world.getTime() <= 0.0f) {
            stack.remove(Grease.GREASE_AGE_COMPONENT_TYPE);
            stack.remove(Grease.GREASE_AGE_COMPONENT_TYPE);
        }
    }

}
