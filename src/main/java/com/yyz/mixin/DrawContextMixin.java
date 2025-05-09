package com.yyz.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yyz.Grease;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color);

    @Shadow @Final private MatrixStack matrices;

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;II)V", at = @At("RETURN"))
    private void injectDrawItemInSlot(TextRenderer textRenderer, ItemStack stack, int x, int y, CallbackInfo ci) {

        if (MinecraftClient.getInstance().world == null) return;
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0f,0.5f,0.5f,0.5f);
        float f = (stack.getOrCreateNbt().getLong("grease_age") - MinecraftClient.getInstance().world.getTime()) / (float)Grease.getConfig().grease_tick;

        if (f > 0.0F) {
            int k = y + MathHelper.floor(16.0F * (1.0F - f));
            int l = k + MathHelper.ceil(16.0F * f);
            this.fill(RenderLayer.getGuiOverlay(), x, k, x + 16, l, Integer.MAX_VALUE);
        }
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.disableBlend();
    }
}
