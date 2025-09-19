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

            newColor.r = scaleComponent(m_color.r, attackProgress);
            newColor.g = scaleComponent(m_color.g, attackProgress);
            newColor.b = scaleComponent(m_color.b, attackProgress);
            newColor.a = scaleComponent(m_color.a, attackProgress);

            for (LampArrayInterop.LampArrayColor lampColor : m_lampColors) {
                lampColor.set(newColor);
            }

            return Math.min(m_lastRepetitionStartTime + repetitionProgress, currentTimeMilliseconds + s_defaultRefreshRate);
        }

        repetitionProgress = OverflowSafeAdd(repetitionProgress, m_sustainDurationMilliseconds);
        boolean inSustainWindow = (repetitionProgress > timeSinceRepetitionStart);
        if (inSustainWindow)
        {
            for (LampArrayInterop.LampArrayColor lampColor : m_lampColors) {
                lampColor.set(m_color);
            }

            return m_lastRepetitionStartTime + repetitionProgress;
        }

        repetitionProgress = OverflowSafeAdd(repetitionProgress, m_decayDurationMilliseconds);
        boolean inDecayWindow = (repetitionProgress > timeSinceRepetitionStart);
        if (inDecayWindow)
        {
            final float decayProgress = (float)(timeSinceRepetitionStart - m_attackDurationMilliseconds - m_sustainDurationMilliseconds) / (float)m_decayDurationMilliseconds;
            LampArrayInterop.LampArrayColor newColor = new LampArrayInterop.LampArrayColor();

            newColor.r = scaleComponent(m_color.r, ((float)1.0 - decayProgress));
            newColor.g = scaleComponent(m_color.g, ((float)1.0 - decayProgress));
            newColor.b = scaleComponent(m_color.b, ((float)1.0 - decayProgress));
            newColor.a = scaleComponent(m_color.a, ((float)1.0 - decayProgress));

            for (LampArrayInterop.LampArrayColor lampColor : m_lampColors) {
                lampColor.set(newColor);
            }

            return Math.min(m_lastRepetitionStartTime + repetitionProgress, currentTimeMilliseconds + s_defaultRefreshRate);
        }

        repetitionProgress = OverflowSafeAdd(repetitionProgress, m_repetitionDelayMilliseconds);
        boolean inDelayWindow = (repetitionProgress > timeSinceRepetitionStart);
        if (inDelayWindow)
        {
            for (LampArrayInterop.LampArrayColor lampColor : m_lampColors) {
                lampColor.set(LampArrayColorConstants.black);
            }

            return m_lastRepetitionStartTime + repetitionProgress;
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
