package com.yyz.grease.fabric;

import com.yyz.grease.GreasePlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class GreasePlatformImpl {
    /**
     * This is our actual method to {@link GreasePlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
