package com.yyz.grease.client;

import com.yyz.grease.Grease;
import com.yyz.grease.entity.ChainLightningRenderer;
import com.yyz.grease.event.ModEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.DyeColor;

import java.util.Arrays;

public class GreaseClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModEvents.registerClient();
        EntityRendererRegistry.register(Grease.Lightning_ENTITY,
                context -> new ChainLightningRenderer(context));
    }

    @Environment(EnvType.CLIENT)
    public static RenderLayer getGlint(String textureType) {
        // 直接从映射中获取对应的渲染层
        RenderLayer layer = GreaseRenderLayer.GLINT_TYPES.get(textureType);

        // 如果找不到指定类型，使用默认的发光效果
        if (layer == null) {
            // 尝试使用小写查找
            layer = GreaseRenderLayer.GLINT_TYPES.get(textureType.toLowerCase());

            // 如果还是找不到，使用默认的发光效果
            if (layer == null) {
                return RenderLayer.getGlint();
            }
        }

        return layer;
    }
}
