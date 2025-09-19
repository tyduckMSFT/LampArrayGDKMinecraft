package com.tyduckmsft.lamparraymod;

public class BlinkEffect extends LampArrayEffect
{
    public BlinkEffect(
        LampArrayInterop.ILampArray lampArray)
    {
        // TODO
        super(lampArray);
    }

    @Override
    public long updateLamps(long currentTimeMilliseconds)
    {
        return s_defaultRefreshRate;
    }
}
