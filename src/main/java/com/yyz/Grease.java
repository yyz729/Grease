package com.yyz;

import com.yyz.config.GreaseConfig;
import com.yyz.event.ModEvents;
import com.yyz.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.fabricmc.fabric.mixin.client.indigo.renderer.ItemRendererMixin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

import java.io.File;

public class Grease implements ModInitializer {
    public static final String MOD_ID = "grease";
    private static GreaseConfig config;

    @Override
    public void onInitialize() {
        config = GreaseConfig.loadConfig(new File(FabricLoader.getInstance().getConfigDir() + "/grease.json"));
        ModItems.register();
        ModEvents.registerServer();
    }

    public static GreaseConfig getConfig() {
        return config;
    }
}
