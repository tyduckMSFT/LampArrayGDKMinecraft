package com.tyduckmsft.lamparraymod;

import org.lwjgl.system.linux.Stat;

import java.security.Key;
import java.util.Arrays;

public class BiomeStaticEffect extends StaticEffect
{
    private static final KeyboardScanCode[] s_navigationKeys =
    { KeyboardScanCode.SC_W, KeyboardScanCode.SC_A, KeyboardScanCode.SC_S, KeyboardScanCode.SC_D, KeyboardScanCode.SC_SPACE };

    private static final KeyboardScanCode[] s_tertiaryKeys =
    { KeyboardScanCode.SC_TAB, KeyboardScanCode.SC_Q, KeyboardScanCode.SC_E, KeyboardScanCode.SC_T, KeyboardScanCode.SC_LEFTSHIFT, KeyboardScanCode.SC_LEFTCONTROL };

    public BiomeStaticEffect(
        LampArrayInterop.ILampArray lampArray,
        LampArrayInterop.LampArrayColor backgroundColor,
        LampArrayInterop.LampArrayColor navigationColor,
        LampArrayInterop.LampArrayColor tertiaryColor,
        long effectDurationMs)
    {
        super(lampArray, effectDurationMs);

        for (int i = 0; i < lampArray.getLampCount(); i++)
        {
            if (lampArray.supportsScanCodes())
            {
                var scanCode = lampArray.getLampInfo(i).getScanCode();
                boolean colorApplied = false;
                for (KeyboardScanCode navigationKey : s_navigationKeys)
                {
                    if (scanCode == navigationKey.getCode())
                    {
                        m_lampColors[i].set(navigationColor);
                        colorApplied = true;
                        break;
                    }
                }
                if (!colorApplied)
                {
                    for (KeyboardScanCode sTertiaryKey : s_tertiaryKeys)
                    {
                        if (scanCode == sTertiaryKey.getCode())
                        {
                            m_lampColors[i].set(tertiaryColor);
                            colorApplied = true;
                            break;
                        }
                    }
                }

                if (!colorApplied)
                {
                    m_lampColors[i].set(backgroundColor);
                }
            }
            else
            {
                m_lampColors[i].set(backgroundColor);
            }
        }
    }
}
