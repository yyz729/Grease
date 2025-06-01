package com.yyz.grease.client;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.joml.Matrix4f;

import java.util.*;

import static com.yyz.grease.Grease.MOD_ID;

public class GreaseRenderLayer extends RenderLayer {

    // 改为Map存储不同贴图类型的渲染层
    public static final Map<String, RenderLayer> GLINT_TYPES = new HashMap<>();
    // 预定义贴图类型
    public static final List<String> TEXTURE_TYPES = Arrays.asList(
            "lightning","fire","freezing","magic","poison","sacred"
    );

    static {
        // 初始化所有贴图类型
        for (String type : TEXTURE_TYPES) {
            GLINT_TYPES.put(type, buildGlintRenderLayer(type));
        }
    }

    public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map) {
        addGlintTypes(map, new ArrayList<>(GLINT_TYPES.values()));
    }

    public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderLayer, BufferBuilder> map, List<RenderLayer> typeList) {
        for (RenderLayer renderType : typeList) {
            if (!map.containsKey(renderType)) {
                map.put(renderType, new BufferBuilder(renderType.getExpectedBufferSize()));
            }
        }
    }

    public GreaseRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer buildGlintRenderLayer(String textureType) {
        // 根据贴图类型构建资源路径
        final Identifier res = new Identifier(MOD_ID, "textures/misc/" + textureType + "_fx.png");

        return RenderLayer.of("glint_" + textureType, VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder()
                .program(RenderPhase.GLINT_PROGRAM)
                .texture(new Texture(res, false, false))
                .writeMaskState(COLOR_MASK)
                .cull(DISABLE_CULLING)
                .depthTest(EQUAL_DEPTH_TEST)
                .transparency(GLINT_TRANSPARENCY)
                .texturing(new OffsetTexturing(0, 0))
                .build(false));
    }


    @Environment(EnvType.CLIENT)
    protected static final class OffsetTexturing extends Texturing {
        public OffsetTexturing(float x, float y) {
            super("offset_texturing_scale", () -> {
                        long l = (long) ((double) Util.getMeasuringTimeMs() * (Double) MinecraftClient.getInstance().options.getGlintSpeed().getValue() * (double) 8.0F);
                        float f = (float) (l % 110000L) / 110000.0F;
                        float g = (float) (l % 30000L) / 30000.0F;
                        RenderSystem.setTextureMatrix((new Matrix4f()).translation(f, g, 0.0F).scale(32));
                    }
                    , RenderSystem::resetTextureMatrix);
        }
    }
}