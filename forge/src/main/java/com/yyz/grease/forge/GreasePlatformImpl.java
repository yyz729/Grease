package com.yyz.grease.forge;

import com.yyz.grease.GreasePlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class GreasePlatformImpl {
    /**
     * This is our actual method to {@link GreasePlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
