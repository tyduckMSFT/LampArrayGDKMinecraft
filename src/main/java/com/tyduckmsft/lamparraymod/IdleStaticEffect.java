package com.tyduckmsft.lamparraymod;

public final class IdleStaticEffect extends StaticEffect
{
    private static final LampArrayInterop.LampArrayColor[] s_brandGreenColors =
    {
        new LampArrayInterop.LampArrayColor((byte) 0x17, (byte) 0x7A, (byte) 0x1E, (byte) 0xFF),
        new LampArrayInterop.LampArrayColor((byte) 0x20, (byte) 0xB2, (byte) 0x37, (byte) 0xFF),
        new LampArrayInterop.LampArrayColor((byte) 0x3F, (byte) 0xCA, (byte) 0x5C, (byte) 0xFF)
    };

    private static final LampArrayInterop.LampArrayColor[] s_brandBrownColors =
    {
        new LampArrayInterop.LampArrayColor((byte) 0x61, (byte) 0x37, (byte) 0x1F, (byte) 0xFF),
        new LampArrayInterop.LampArrayColor((byte) 0x85, (byte) 0x4F, (byte) 0x2B, (byte) 0xFF),
        new LampArrayInterop.LampArrayColor((byte) 0xC2, (byte) 0x83, (byte) 0x40, (byte) 0xFF)
    };

    public IdleStaticEffect(LampArrayInterop.ILampArray lampArray, long effectDurationMs)
    {
        super(lampArray, effectDurationMs);

        // For keyboards, the top half of the device (y dimension) will use random greens
        // Bottom half of the device will use random browns
        LampArrayInterop.LampArrayPosition boundingBox = lampArray.getBoundingBox();
        for (int i = 0; i < lampArray.getLampCount(); i++) {
            if (lampArray.getKind() == LampArrayInterop.LampArrayKind.Keyboard) {
                var lampPosition = lampArray.getLampInfo(i).getPosition();
                if (lampPosition.yInMeters < (boundingBox.yInMeters / 2.0)) {
                    m_lampColors[i].set(s_brandGreenColors[i % 3]);
                } else {
                    m_lampColors[i].set(s_brandBrownColors[i % 3]);
                }
            }
            else {
                m_lampColors[i].set(LampArrayColorConstants.minecraftGreen);
            }
        }
    }
}
