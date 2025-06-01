package com.yyz.grease.mixin;

import com.yyz.grease.item.GreaseItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void injectInventoryTick(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (item.getItem() instanceof GreaseItem) {
            ci.cancel();
            matrices.push();
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();

            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
                this.applyEquipOffset(matrices, arm, equipProgress);
                matrices.translate((float) i * -0.4785682F - i * 0.2, -0.094387F, 0.05731531F- (bl3 ? 0.45 : 0));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 65.3F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) i * -9.785F));
                float f = (float) item.getMaxUseTime() - ((float) this.client.player.getItemUseTimeLeft() - tickDelta + 1.0F);
                float g = f / (float) 25;
                if (g > 1.0F) {
                    g = 1.0F;
                }

                if (g > 0.1F) {
                    float h = MathHelper.sin((f - 0.1F) * 1.3F);
                    float j = g - 0.1F;
                    float k = h * j;
                    matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
                }

                matrices.translate(g * -( bl3 ? 0.1F : 0.3F), g * ( bl3 ? 0.6F:0.9F), g * -0.5F );
                matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) i * 45.0F));

            } else {
                this.applyEquipOffset(matrices, arm, equipProgress);
                this.applySwingOffset(matrices, arm, swingProgress);
            }
            this.renderItem(player, item, ModelTransformationMode.FIRST_PERSON_LEFT_HAND, true, matrices, vertexConsumers, light);
            matrices.pop();
        }
        Hand target = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        matrices.push();
        if (item.getItem() instanceof ToolItem && player.isUsingItem()) {
           ci.cancel();
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;

            this.applyEquipOffset(matrices, arm, equipProgress);
            matrices.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 65.3F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) i * -9.785F));
            float f = (float) player.getStackInHand(target).getMaxUseTime() - ((float) this.client.player.getItemUseTimeLeft() - tickDelta + 1.0F);
            float g = f / (float) 25;
            if (g > 1.0F) {
                g = 1.0F;
            }

            if (g > 0.1F) {
                float h = MathHelper.sin((f - 0.1F) * 1.3F);
                float j = g - 0.1F;
                float k = h * j;
                matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
            }

            matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
            matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) i * 45.0F));
            this.renderItem(player, item, ModelTransformationMode.FIRST_PERSON_LEFT_HAND, true, matrices, vertexConsumers, light);
        }

        matrices.pop();
    }
}
