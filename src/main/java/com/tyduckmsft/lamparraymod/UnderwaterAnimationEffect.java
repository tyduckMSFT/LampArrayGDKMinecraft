package com.tyduckmsft.lamparraymod;

import java.util.Random;

public class UnderwaterAnimationEffect extends AnimationEffect
{
    private final int[] m_blueValues;
    private final int[] m_greenOffsets;
    private final boolean[] m_blueValuesDescending;

    private long m_effectDurationMilliseconds = 0;

    public UnderwaterAnimationEffect(
        LampArrayInterop.ILampArray lampArray,
        long updateIntervalMilliseconds,
        long effectDurationMilliseconds)
    {
        super(lampArray, updateIntervalMilliseconds);

        m_effectDurationMilliseconds = effectDurationMilliseconds;
        m_blueValues = new int[m_lampIndices.length];
        m_greenOffsets = new int[m_lampIndices.length];
        m_blueValuesDescending = new boolean[m_lampIndices.length];

        for (int i = 0; i < m_blueValues.length; i++)
        {
            m_lampColors[i].a = (byte)0xFF;
            m_lampColors[i].b = (byte)(int)(Math.random() * 256);
            m_blueValues[i] = m_lampColors[i].b;
            m_greenOffsets[i] = 255 / ((int)(Math.random() * 4) + 1);
        }
    }

    @Override
    public void updateLampsImpl(long currentTimeMilliseconds)
    {
        m_isDirty = true;
        for (int i = 0; i < m_blueValues.length; i++) {
            if (m_blueValues[i] >= 0xFF) {
                m_blueValuesDescending[i] = true;
                m_blueValues[i] = 0xFF;

            } else if (m_blueValues[i] <= 0) {
                m_blueValuesDescending[i] = false;
                m_blueValues[i] = 0;
            }

            if (m_blueValuesDescending[i]) {
                m_blueValues[i] = Math.max(0, m_blueValues[i] - 2);
            } else {
                m_blueValues[i] = Math.min(0xFF, m_blueValues[i] + 2);
            }

            m_lampColors[i].b = (byte) m_blueValues[i];
            // m_lampColors[i].g = (byte) (m_blueValues[i] / m_greenOffsets[i]);
        }
    }
}
