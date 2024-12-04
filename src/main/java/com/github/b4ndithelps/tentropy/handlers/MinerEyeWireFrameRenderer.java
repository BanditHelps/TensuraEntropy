package com.github.b4ndithelps.tentropy.handlers;

import com.github.b4ndithelps.tentropy.TensuraEntropy;
import com.github.b4ndithelps.tentropy.effect.MinerEyeEffect;
import com.github.b4ndithelps.tentropy.effect.ModEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = TensuraEntropy.MODID, value = Dist.CLIENT)
public class MinerEyeWireFrameRenderer {

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null ||
            !mc.player.hasEffect(ModEffects.MINER_EYE_EFFECT.get())) {
            return;
        }

        // Get the ores
        Set<BlockPos> detectedOres = MinerEyeEffect.getDetectedOres();
        if (detectedOres.isEmpty()) return;

        // Get the camera's position
        PoseStack poseStack = event.getPoseStack();
        Camera camera = mc.gameRenderer.getMainCamera();

        // Begin the rendering
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        poseStack.pushPose();

        // Adjust for camera position
        poseStack.translate(
                -camera.getPosition().x(),
                -camera.getPosition().y(),
                -camera.getPosition().z()
        );

        for (BlockPos pos : detectedOres) {
            renderBlockWireframe(poseStack, pos, 1.0f, 0.7f, 0.0f, 0.5f);
        }

        poseStack.popPose();

        // Reset the render states
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.lineWidth(1.0f);

    }

    private static void renderBlockWireframe(PoseStack poseStack, BlockPos pos, float red, float green, float blue, float alpha) {
        // Slighlty expand the box so it is outside the box
        AABB box = new AABB(pos).inflate(0.005);

        // Thick Boys
        RenderSystem.lineWidth(3.0f);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();

        Matrix4f matrix = poseStack.last().pose();

        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        addWireframeLines(bufferBuilder, matrix, box, red, green, blue, alpha);

        BufferUploader.drawWithShader(bufferBuilder.end());

    }

    private static void addWireframeLines(BufferBuilder bufferBuilder, Matrix4f matrix, AABB box, float red, float green, float blue, float alpha) {
        // Bottom face
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();

        // Top face
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();

        // Vertical lines
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.minZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.maxX, (float)box.maxY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();

        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();
        bufferBuilder.vertex(matrix, (float)box.minX, (float)box.maxY, (float)box.maxZ)
                .color(red, green, blue, alpha).endVertex();
    }



//    private static final Set<BlockPos> highlightedBlocks = new HashSet<>();
//
//    public static void addBlockPos(BlockPos pos) {
//        highlightedBlocks.add(pos);
//    }
//
//    public static void clearHighlights() {
//         highlightedBlocks.clear();
//    }
//
//    @SubscribeEvent
//    public static void onRenderWorld(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
//            return;
//        }
//
//        Minecraft mc = Minecraft.getInstance();
//        Level level = mc.level;
//
//        if (level == null || highlightedBlocks.isEmpty()) {
//            return;
//        }
//
//        PoseStack poseStack = event.getPoseStack();
//        VertexConsumer buffer = Tesselator.getInstance().getBuilder();
//
//        // Loop through all the highlighted blocks and add an outline
//        for (BlockPos pos : highlightedBlocks) {
//            renderBlockOutline(poseStack, buffer, pos, 1.0f, 1.0f, 0.0f, 1.0f);
//        }
//
//    }


//    private static void renderBlockOutline(PoseStack poseStack, VertexConsumer buffer, BlockPos pos, float r, float g, float b, float alpha) {
//        Minecraft mc = Minecraft.getInstance();
//
//        poseStack.pushPose();
//        poseStack.translate(pos.getX() - mc.gameRenderer.getMainCamera().getPosition().x,
//                pos.getY() - mc.gameRenderer.getMainCamera().getPosition().y,
//                pos.getZ() - mc.gameRenderer.getMainCamera().getPosition().z);
//
//        // Use GL-based rendering
//        RenderType renderType = RenderType.lines();
//        .drawBoundingBox(poseStack, buffer, renderType, r, g, b, alpha);
//
//
//        poseStack.popPose();
//    }
}
