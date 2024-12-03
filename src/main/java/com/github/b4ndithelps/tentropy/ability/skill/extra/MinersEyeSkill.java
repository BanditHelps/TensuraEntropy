package com.github.b4ndithelps.tentropy.ability.skill.extra;

import com.github.b4ndithelps.tentropy.TensuraEntropy;
import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import com.github.manasmods.tensura.ability.SkillHelper;
import com.github.manasmods.tensura.ability.SkillUtils;
import com.github.manasmods.tensura.ability.TensuraSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import com.github.manasmods.tensura.capability.race.TensuraPlayerCapability;
import com.github.manasmods.tensura.race.slime.SlimeRace;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Objects;

/**
 * Scans all the ores around the player in a different radius
 */
public class MinersEyeSkill extends Skill {

    private final double epUnlockCost = 60000.0;

    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(TensuraEntropy.MODID, "textures/skill/extra/miners_eye.png");
    }

    public MinersEyeSkill() {
        super(SkillType.EXTRA);
    }

    public double learningCost() {
        return 50.0;
    }

    protected boolean canActivateInRaceLimit(ManasSkillInstance instance) {
        return true;
    }

    // TODO make this based on ores mined
    public boolean meetEPRequirement(Player entity, double curEP) {
        return curEP >= epUnlockCost;
    }

    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity living) {
        return instance.isMastered(living);
    }

    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isMastered(entity) && instance.isToggled();
    }

    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        return 10.0;
    }

    public boolean onHeld(ManasSkillInstance instance, LivingEntity living, int heldTicks) {
        if (heldTicks % 20 == 0 && SkillHelper.outOfMagicule(living, instance)) {
            return false;
        } else {
            if (heldTicks % 30 == 0 && heldTicks > 0) {
                this.addMasteryPoint(instance, living);
            }

            living.addEffect(new MobEffectInstance((MobEffect) TensuraMobEffects.PRESENCE_SENSE.get(), 20, 1, false, false));
            return true;
        }
    }

    public void onTick(ManasSkillInstance instance, LivingEntity entity) {
        if (SkillHelper.outOfMagicule(entity, this.magiculeCost(entity, instance) * 5.0)) {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                player.displayClientMessage(Component.translatable("tensura.skill.lack_magicule.toggled_off", new Object[]{instance.getSkill().getName()}).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
            }

            instance.setToggled(false);
        } else {
            entity.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.PRESENCE_SENSE.get(), 200, 1, false, false, false));
        }
    }

    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        this.onTick(instance, entity);
        entity.getLevel().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        if (entity.hasEffect((MobEffect)TensuraMobEffects.PRESENCE_SENSE.get())) {
            int level = ((MobEffectInstance) Objects.requireNonNull(entity.getEffect((MobEffect)TensuraMobEffects.PRESENCE_SENSE.get()))).getAmplifier();
            if (level == 1) {
                entity.removeEffect((MobEffect)TensuraMobEffects.PRESENCE_SENSE.get());
            }
        }

    }

    private boolean isOre(Block block) {
        return block == Blocks.COAL_ORE ||
                block == Blocks.IRON_ORE ||
                block == Blocks.GOLD_ORE ||
                block == Blocks.DIAMOND_ORE ||
                block == Blocks.EMERALD_ORE ||
                block == Blocks.REDSTONE_ORE ||
                block == Blocks.LAPIS_ORE ||
                block == Blocks.DEEPSLATE_COAL_ORE ||
                block == Blocks.DEEPSLATE_IRON_ORE ||
                block == Blocks.DEEPSLATE_GOLD_ORE ||
                block == Blocks.DEEPSLATE_DIAMOND_ORE ||
                block == Blocks.DEEPSLATE_EMERALD_ORE ||
                block == Blocks.DEEPSLATE_REDSTONE_ORE ||
                block == Blocks.DEEPSLATE_LAPIS_ORE;
    }
}
