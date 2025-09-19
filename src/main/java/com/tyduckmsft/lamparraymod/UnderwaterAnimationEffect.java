package com.tyduckmsft.lamparraymod;

public class UnderwaterAnimationEffect extends AnimationEffect
{
    public UnderwaterAnimationEffect(
        LampArrayInterop.ILampArray lampArray,
        int[] indices,
        long updateIntervalMilliseconds)
    {
        super(lampArray, indices, updateIntervalMilliseconds);
    }

    @Override
    public void updateLampsImpl()
    {

    }
}
