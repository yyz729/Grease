package com.yyz.grease.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yyz.grease.client.GreaseClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyArg(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"), index = 5)
    private VertexConsumer injected(VertexConsumer vertices, @Local(argsOnly = true)ItemStack stack,@Local(argsOnly = true) VertexConsumerProvider vertexConsumers) {
        if(!stack.getOrCreateNbt().getString("grease").isEmpty()) {
            return VertexConsumers.union(vertexConsumers.getBuffer(GreaseClient.getGlint(stack.getOrCreateNbt().getString("grease"))), vertices);
        }
        return vertices;
    }
}
