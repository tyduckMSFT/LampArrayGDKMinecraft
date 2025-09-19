package com.tyduckmsft.lamparraymod;

public abstract class AnimationEffect extends LampArrayEffect
{
    protected long m_updateIntervalMilliseconds;

    public AnimationEffect(
        LampArrayInterop.ILampArray lampArray,
        long updateIntervalMilliseconds)
    {
        super (lampArray);

        // This is a heuristic to ensure our update interval is something manageable to work with.
        if (updateIntervalMilliseconds > 60000)
        {
            throw new IllegalArgumentException("Animation effect refresh rate must be at least every 60 seconds");
        }

        m_updateIntervalMilliseconds = updateIntervalMilliseconds;
    }

    public AnimationEffect(
        LampArrayInterop.ILampArray lampArray,
        int[] indices,
        long updateIntervalMilliseconds)
    {
        super(lampArray, indices);

        // This is a heuristic to ensure our update interval is something manageable to work with.
        if (updateIntervalMilliseconds > 60000)
        {
            throw new IllegalArgumentException("Animation effect refresh rate must be at least every 60 seconds");
        }

        m_updateIntervalMilliseconds = updateIntervalMilliseconds;
    }

    /*
     * Returns the due time of the next effect update.
     */
    @Override
    public long updateLamps(long currentTimeMilliseconds)
    {
        long timeOfPreviousUpdate = m_lastUpdateTimeMilliseconds;
        updateLampsImpl(currentTimeMilliseconds);
        updateTimestamps();

        long thisUpdateDuration = m_lastUpdateTimeMilliseconds - timeOfPreviousUpdate;
        if (thisUpdateDuration > m_updateIntervalMilliseconds)
        {
            return currentTimeMilliseconds;
        }

        return currentTimeMilliseconds + (m_updateIntervalMilliseconds - thisUpdateDuration);
    }

    public abstract void updateLampsImpl(long currentTimeMilliseconds);
}
