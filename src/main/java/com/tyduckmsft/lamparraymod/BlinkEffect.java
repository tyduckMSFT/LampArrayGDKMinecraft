package com.tyduckmsft.lamparraymod;

import java.util.Arrays;

public class BlinkEffect extends LampArrayEffect
{
    private long m_attackDurationMilliseconds;
    private long m_sustainDurationMilliseconds;
    private long m_decayDurationMilliseconds;
    private long m_repetitionDelayMilliseconds;
    private int m_targetOccurrencesCount;
    private int m_elapsedOccurrencesCount = 0;

    private long m_lastRepetitionStartTime = 0;
    private boolean m_needsRestart = true;

    private LampArrayInterop.LampArrayColor m_color;

    public BlinkEffect(
        LampArrayInterop.ILampArray lampArray,
        LampArrayInterop.LampArrayColor color,
        long attackDurationMilliseconds,
        long sustainDurationMilliseconds,
        long decayDurationMilliseconds,
        long repetitionDelayMilliseconds,
        int occurrencesCount)
    {
        super(lampArray);

        m_color = color;
        m_attackDurationMilliseconds = attackDurationMilliseconds;
        m_sustainDurationMilliseconds = sustainDurationMilliseconds;
        m_decayDurationMilliseconds = decayDurationMilliseconds;
        m_repetitionDelayMilliseconds = repetitionDelayMilliseconds;
        m_targetOccurrencesCount = occurrencesCount;
    }

    @Override
    public long updateLamps(long currentTimeMilliseconds)
    {
        if (m_needsRestart)
        {
            m_lastRepetitionStartTime = currentTimeMilliseconds;
            m_needsRestart = false;
        }

        final int lampCount = m_lampArray.getLampCount();
        final long timeSinceRepetitionStart = currentTimeMilliseconds - m_lastRepetitionStartTime;
        m_isDirty = true;

        long repetitionProgress = m_attackDurationMilliseconds;
        boolean inAttackWindow = (repetitionProgress > timeSinceRepetitionStart);
        if (inAttackWindow)
        {
            final float attackProgress = (float)timeSinceRepetitionStart / (float)m_attackDurationMilliseconds;
            LampArrayInterop.LampArrayColor newColor = new LampArrayInterop.LampArrayColor();

            newColor.r = (byte)(((float)attackProgress / (float)repetitionProgress) * m_color.r);
            newColor.g = (byte)(((float)attackProgress / (float)repetitionProgress) * m_color.g);
            newColor.b = (byte)(((float)attackProgress / (float)repetitionProgress) * m_color.b);
            newColor.a = (byte)(((float)attackProgress / (float)repetitionProgress) * m_color.a);

            Arrays.fill(m_lampColors, newColor);
            return Math.min(s_defaultRefreshRate, repetitionProgress - timeSinceRepetitionStart);
        }

        repetitionProgress = OverflowSafeAdd(repetitionProgress, m_sustainDurationMilliseconds);
        boolean inSustainWindow = (repetitionProgress > timeSinceRepetitionStart);
        if (inSustainWindow)
        {
            for (int i = 0; i < lampCount; i++)
            {
                m_lampColors[i].set(m_color);
            }

            System.out.println("Sustain");
            return repetitionProgress - currentTimeMilliseconds;
        }

        repetitionProgress = OverflowSafeAdd(repetitionProgress, m_decayDurationMilliseconds);
        boolean inDecayWindow = (repetitionProgress > timeSinceRepetitionStart);
        if (inDecayWindow)
        {
            // TODO
            System.out.println("Decay");
            return s_defaultRefreshRate;
        }

        repetitionProgress = OverflowSafeAdd(repetitionProgress, m_repetitionDelayMilliseconds);
        boolean inDelayWindow = (repetitionProgress > timeSinceRepetitionStart);
        if (inDelayWindow)
        {
            for (int i = 0; i < lampCount; i++)
            {
                m_lampColors[i].set(LampArrayColorConstants.black);
            }

            System.out.println("RepDelay");
            return repetitionProgress - timeSinceRepetitionStart;
        }

        // We have finished the current occurrence of this effect. Calculate whether we need another.
        m_elapsedOccurrencesCount++;
        if (m_elapsedOccurrencesCount >= m_targetOccurrencesCount)
        {
            m_isCompleted = true;
        }
        else
        {
            m_needsRestart = true;
        }

        return 0;
    }
}
