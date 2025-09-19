package com.tyduckmsft.lamparraymod;

public abstract class AnimationEffect extends LampArrayEffect
{
    protected long m_refreshRateMilliseconds;

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

        m_refreshRateMilliseconds = updateIntervalMilliseconds;
    }

    @Override
    public long updateLamps(long currentTimeMilliseconds)
    {
        updateTimestamps();
        updateLampsImpl();

        long timePostUpdate = System.currentTimeMillis();
        long timeDiff = System.currentTimeMillis() - (currentTimeMilliseconds + m_refreshRateMilliseconds);

        if (timeDiff > 0)
        {
            return 0;
        }

        return m_refreshRateMilliseconds;
    }

    public abstract void updateLampsImpl();
}
