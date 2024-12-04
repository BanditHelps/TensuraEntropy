package com.github.b4ndithelps.tentropy.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class MinerEyeEffect extends MobEffect {

    private static final Set<BlockPos> detectedOres = new HashSet<>();

    // Pull in the magic ore from the Tensura Mod
    private static final Block MAGIC_ORE = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("tensura", "magic_ore"));
    private static final Block DEEPSLATE_MAGIC_ORE = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("tensura", "deepslate_magic_ore"));
    private static final Block SILVER_ORE = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("tensura", "silver_ore"));
    private static final Block DEEPSLATE_SILVER_ORE = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("tensura", "deepslate_silver_ore"));

    // Sets to store the ores that can be detected
    private static final Set<Block> SIMPLE_ORES = Set.of(
            Blocks.COAL_ORE,
            Blocks.IRON_ORE,
            Blocks.COPPER_ORE,
            Blocks.LAPIS_ORE,
            SILVER_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            DEEPSLATE_SILVER_ORE
    );

    private static final Set<Block> MASTERED_ORES = Set.of(
            Blocks.COAL_ORE,
            Blocks.IRON_ORE,
            Blocks.GOLD_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.EMERALD_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.COPPER_ORE,
            Blocks.LAPIS_ORE,
            MAGIC_ORE,
            SILVER_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            DEEPSLATE_MAGIC_ORE,
            DEEPSLATE_SILVER_ORE
    );


    protected MinerEyeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player) || !player.level.isClientSide) {
            return;
        }

        Level level = player.getLevel();
        int radius = 8 + amplifier * 2;
        BlockPos playerPos = player.blockPosition();

        detectedOres.clear();

        // Radius 8x8x8 - amplifier applies to it
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Position of block to be scanned
                    BlockPos pos = playerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(pos);
                    Block block = blockState.getBlock();

                    if (isOre(block, amplifier)) {
                        detectedOres.add(pos);
                    }
                }
            }
        }

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // Every 1 second
        return duration % 20 == 0;
    }

    /**
     * When no amplifier is applied, just detects Coal, Iron, Lapis, Copper
     *
     * If amplifier is higher, will detect All Ore Types
     */
    public boolean isOre(Block block, int amplifier) {
        return amplifier == 0
                ? SIMPLE_ORES.contains(block)
                : MASTERED_ORES.contains(block);
    }

    public static Set<BlockPos> getDetectedOres() {
        return detectedOres;
    }
}
