package com.tyduckmsft.lamparraymod;

/*
 * This is the base class for implementing effect animations.
 * It is not thread-safe.
 */
public abstract class LampArrayEffect implements AutoCloseable
{
    protected static final int s_defaultRefreshRate = 33;

    protected LampArrayInterop.ILampArray m_lampArray = null;

    protected int[] m_lampIndices = null;
    protected LampArrayInterop.LampArrayColor[] m_lampColors = null;

    protected boolean m_isCompleted = false;
    protected boolean m_isDirty = true;

    protected long m_effectStartTimeMilliseconds = 0;
    protected long m_lastUpdateTimeMilliseconds = 0;

    public LampArrayEffect(
        LampArrayInterop.ILampArray lampArray)
    {
        m_lampArray = lampArray;
        m_lampArray.AddRef();

        m_lampIndices = new int[lampArray.getLampCount()];
        for (int i = 0; i < m_lampIndices.length; i++)
        {
            m_lampIndices[i] = i;
        }

        initializeLampColors();

        updateTimestamps();
    }

    public LampArrayEffect(
        LampArrayInterop.ILampArray lampArray,
        int[] lampIndices)
    {
        m_lampArray = lampArray;
        m_lampArray.AddRef();

        m_lampIndices = new int[lampIndices.length];
        System.arraycopy(lampIndices, 0, m_lampIndices, 0, lampIndices.length);

        initializeLampColors();

        updateTimestamps();
    }

    private void initializeLampColors()
    {
        m_lampColors = (LampArrayInterop.LampArrayColor[]) new LampArrayInterop.LampArrayColor().toArray(m_lampIndices.length);
    }

    @Override
    public void close()
    {
        m_lampArray.Release();
        m_lampArray = null;
    }

    // duration
    // frame rate

    // Returns the due time for the next update.
    public abstract long updateLamps(long currentTimeMilliseconds);

    public void stop() {}

    public void sendLampUpdate()
    {
        if (m_isDirty)
        {
            try
            {
                m_lampArray.setColorsForIndices(m_lampIndices, m_lampColors);
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.toString());
            }
            m_isDirty = false;
        }
    }

    public boolean isCompleted()
    {
        return m_isCompleted;
    }

    public static long OverflowSafeAdd(long long1, long long2)
    {
        try
        {
            return Math.addExact(long1, long2);
        }
        catch (ArithmeticException e)
        {
            return Long.MAX_VALUE;
        }
    }

    public static byte scaleComponent(byte value, float factor) {
        int unsigned = Byte.toUnsignedInt(value); // Convert to 0–255
        int scaled = Math.round(unsigned * factor);
        int clamped = Math.max(0, Math.min(scaled, 255));
        return (byte) clamped;
    }

    protected void updateTimestamps()
    {
        if (m_effectStartTimeMilliseconds == 0)
        {
            m_effectStartTimeMilliseconds = System.currentTimeMillis();
        }

        m_lastUpdateTimeMilliseconds = System.currentTimeMillis();
    }
};
