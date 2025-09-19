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
                /*
                if (Arrays.asList(s_navigationKeys).contains((KeyboardScanCode) scanCode))
                {
                    m_lampColors[i].set(navigationColor);
                }
                else if (Arrays.asList(s_tertiaryKeys).contains(scanCode))
                {
                    m_lampColors[i].set(tertiaryColor);
                }
                else

                 */
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
