package com.github.b4ndithelps.tentropy.effect;

import com.github.b4ndithelps.tentropy.handlers.HighlightHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class MinerEyeEffect extends MobEffect {


    protected MinerEyeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player) || player.level.isClientSide) {
            // Only run if the entity is a player or it is ran on a client
            return;
        }

        Level level = player.getLevel();
        int radius = 8 + amplifier * 2;
        BlockPos playerPos = player.blockPosition();

        Set<BlockPos> detectedOres = new HashSet<>();

        // Radius 8x8x8 - amplifier applies to it
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Position of block to be scanned
                    BlockPos pos = playerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(pos);
                    Block block = blockState.getBlock();

                    if (isOre(block)) {
                        player.displayClientMessage(Component.literal("You found a " + block.getName().toString() + "at " + x + ", " + y + ", " + z), false);
//                        detectedOres.add(pos);
                    }
                }
            }
        }

        if (!detectedOres.isEmpty()) {
//            HighlightHandler.clearHighlights();
//            detectedOres.forEach(HighlightHandler::addBlockPos);
        }


//        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Every 1 second
        return duration % 20 == 0;
    }

    private boolean isOre(Block block) {
        return block == Blocks.COAL_ORE;
    }
}
