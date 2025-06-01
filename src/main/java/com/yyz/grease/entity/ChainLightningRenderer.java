package com.yyz.grease.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ChainLightningRenderer extends EntityRenderer<CustomLightningEntity> {
    private static final float LINE_WIDTH = 0.3F;
    private static final int SEGMENTS = 8;
    private static final Random RANDOM = Random.create();

    public ChainLightningRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(CustomLightningEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vcp, int light) {
        List<Entity> targets = entity.getTargets();
        if (targets.size() < 2) return;

        VertexConsumer buffer = vcp.getBuffer(RenderLayer.getLightning());
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // 生成生物间连接路径
        List<Vec3d> pathPoints = generatePath(targets,tickDelta);
        
        // 渲染闪电路径
        renderLightningChain(matrix, buffer, pathPoints);
    }

    private List<Vec3d> generatePath(List<Entity> targets,float tickDelta) {
        List<Vec3d> points = new ArrayList<>();
        
        // 生成生物间的中点
        for (int i = 0; i < targets.size() - 1; i++) {
            Entity start = targets.get(i);
            Entity end = targets.get(i+1);
            
            Vec3d startPos = start.getLerpedPos(tickDelta);
            Vec3d endPos = end.getLerpedPos(tickDelta);
            
            // 添加带有随机偏移的点
            for (int j = 0; j < SEGMENTS; j++) {
                double progress = (double)j / SEGMENTS;
                double offsetX = RANDOM.nextGaussian() * 0.5;
                double offsetY = RANDOM.nextGaussian() * 0.5;
                double offsetZ = RANDOM.nextGaussian() * 0.5;
                
                points.add(startPos.lerp(endPos, progress)
                    .add(offsetX, offsetY, offsetZ));
            }
        }
        return points;
    }

    private void renderLightningChain(Matrix4f matrix, VertexConsumer buffer, 
                                     List<Vec3d> points) {
        float red = 0.45F;
        float green = 0.45F;
        float blue = 0.5F;
        float alpha = 0.3F;

        for (int i = 0; i < points.size() - 1; i++) {
            Vec3d current = points.get(i);
            Vec3d next = points.get(i+1);

            // 构建四边形线段
            buildLightningQuad(matrix, buffer, current, next, 
                red, green, blue, alpha);
        }
    }

    private void buildLightningQuad(Matrix4f matrix, VertexConsumer buffer,
                                   Vec3d start, Vec3d end,
                                   float r, float g, float b, float a) {
        Vec3d normal = end.subtract(start).normalize();
        Vec3d perpendicular = new Vec3d(-normal.z, 0, normal.x).normalize().multiply(LINE_WIDTH);

        // 四个顶点
        Vec3d[] vertices = {
            start.add(perpendicular),
            start.subtract(perpendicular),
            end.subtract(perpendicular),
            end.add(perpendicular)
        };

        // 构建四边形
        for (int i = 0; i < 4; i++) {
            Vec3d v = vertices[i];
            buffer.vertex(matrix, (float)v.x, (float)v.y, (float)v.z)
                .color(r, g, b, a)
                .next();
        }
    }

    @Override
    public Identifier getTexture(CustomLightningEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public boolean shouldRender(CustomLightningEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}