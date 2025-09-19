package com.tyduckmsft.lamparraymod;

public class LampArrayEffectFactory
{
    public LampArrayEffect CreateEffect(
            MinecraftEffectState.EffectType effectType,
            LampArrayInterop.ILampArray lampArray)
    {
        return switch (effectType)
        {
            case Idle -> new IdleStaticEffect(lampArray, Long.MAX_VALUE);
            case Biome ->
                    new BiomeStaticEffect(lampArray, LampArrayColorConstants.green, LampArrayColorConstants.red, LampArrayColorConstants.white, Long.MAX_VALUE);
            case Damage -> new BlinkEffect(lampArray);
            default -> null;
        };
    }
}
