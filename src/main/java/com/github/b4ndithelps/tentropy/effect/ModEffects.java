package com.github.b4ndithelps.tentropy.effect;

import com.github.b4ndithelps.tentropy.TensuraEntropy;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TensuraEntropy.MODID);

    public static final RegistryObject<MinerEyeEffect> MINER_EYE_EFFECT = MOB_EFFECTS.register("miner_eye",
            () -> new MinerEyeEffect(MobEffectCategory.NEUTRAL, 0xFF0000));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }


}
