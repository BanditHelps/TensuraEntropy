package com.github.b4ndithelps.tentropy.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HighlightHandler {

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
