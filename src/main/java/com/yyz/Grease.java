package com.yyz;

import com.yyz.config.GreaseConfig;
import com.yyz.event.ModEvents;
import com.yyz.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.mojang.serialization.Codec;

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

    public static final ComponentType<String> GREASE_COMPONENT_TYPE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(MOD_ID, "grease"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static final ComponentType<Long> GREASE_AGE_COMPONENT_TYPE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(MOD_ID, "grease_age"),
            ComponentType.<Long>builder().codec(Codec.LONG).build()
    );
}
