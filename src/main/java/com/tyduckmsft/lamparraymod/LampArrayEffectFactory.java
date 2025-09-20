package com.tyduckmsft.lamparraymod;

public class LampArrayEffectFactory
{
    public LampArrayEffect CreateEffect(
            MinecraftLightingEffectState effectType,
            LampArrayInterop.ILampArray lampArray)
    {
        return switch (effectType)
        {
            case Idle -> new IdleStaticEffect(lampArray, Long.MAX_VALUE);
            case Biome ->
                    new BiomeStaticEffect(
                            lampArray,
                            LampArrayColorConstants.green,
                            LampArrayColorConstants.red,
                            LampArrayColorConstants.white,
                            Long.MAX_VALUE);
            case Damage -> new BlinkEffect(
                    lampArray,
                    LampArrayColorConstants.red,
                    100,
                    100,
                    500,
                    100,
                    3);
            case Death -> new BlinkEffect(
                    lampArray,
                    LampArrayColorConstants.darkRed,
                    1000,
                    Long.MAX_VALUE,
                    Long.MAX_VALUE,
                    0,
                    1);
            case Underwater -> new UnderwaterAnimationEffect(
                    lampArray,
                    33,
                    Long.MAX_VALUE);
            default -> null;
        };
    }
}
