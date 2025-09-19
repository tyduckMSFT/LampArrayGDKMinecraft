package com.tyduckmsft.lamparraymod;

import java.util.Arrays;

/*
 * Represents a static effect that runs until duration has elapsed or effect is stopped.
 * Prepopulated with index and color data at creation time.
 */
public class StaticEffect extends LampArrayEffect
{
    protected long m_effectDurationMilliseconds = 0;

    public StaticEffect(
        LampArrayInterop.ILampArray lampArray,
        long effectDurationMs)
    {
        super(lampArray);

        m_isDirty = true;
        m_effectDurationMilliseconds = effectDurationMs;
    }

    public StaticEffect(
        LampArrayInterop.ILampArray lampArray,
        LampArrayInterop.LampArrayColor[] colors,
        int[] lampIndices,
        long effectDurationMs)
    {
        super(lampArray, lampIndices);

        if (colors.length != lampIndices.length)
        {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < colors.length; i++)
        {
            m_lampColors[i].set(colors[i]);
        }

        m_isDirty = true;
        m_effectDurationMilliseconds = effectDurationMs;
    }

    public long updateLamps(long currentTimeMilliseconds)
    {
        updateTimestamps();

        long dueTime = OverflowSafeAdd(m_effectStartTimeMilliseconds, m_effectDurationMilliseconds);
        if (currentTimeMilliseconds >= dueTime)
        {
            System.out.println("Effect completed");
            m_isCompleted = true;
        }

        return dueTime;
    }

    public void stop()
    {
        Arrays.fill(m_lampColors, LampArrayColorConstants.black);

        m_isDirty = true;
        sendLampUpdate();
    }
}
