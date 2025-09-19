package com.tyduckmsft.lamparraymod;

import com.sun.jna.*;
import com.sun.jna.ptr.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LampArrayDeviceManager implements AutoCloseable
{
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean CheckWinOS()
    {
        return OS.contains("win");
    }

    static
    {
        if (CheckWinOS())
        {
            try
            {
                // Load the Windows GDK API
                System.out.println("Looking for LampArray DLL");
                System.loadLibrary("LampArray");
                System.out.println("LampArray DLL loaded!");
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
        else
        {
            System.out.println("LampArrayMod requires Windows 11 23H2 or above.");
        }
    }

    private static class LampArrayEffectContext
    {
        LampArrayInterop.ILampArray lampArray;
        LampArrayEffect effect;
    };

    private static final ScheduledExecutorService s_scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture s_scheduledTask = null;

    private static LongByReference s_callbackToken = new LongByReference();

    private static Lock s_lampArraysLock = new ReentrantLock();
    private static List<LampArrayEffectContext> s_lampArrays = new ArrayList<LampArrayEffectContext>();

    private static LampArrayEffectFactory s_effectFactory = new LampArrayEffectFactory();

    private static boolean s_effectTypeDirty = true;
    private static MinecraftEffectState.EffectType s_effectType = MinecraftEffectState.EffectType.Idle;

    private static LampArrayInterop.LampArrayStatusCallback OnLampArrayStatusChanged = new LampArrayInterop.LampArrayStatusCallback()
    {
        @Override
        public void invoke(Pointer context, int currentStatus, int previousStatus, Pointer lampArrayPtr)
        {
            boolean wasConnected = (previousStatus & LampArrayInterop.LampArrayStatus.Connected.value) != 0;
            boolean isConnected = (currentStatus & LampArrayInterop.LampArrayStatus.Connected.value) != 0;

            try
            {
                s_lampArraysLock.lock();

                LampArrayInterop.ILampArray lampArray = new LampArrayInterop.ILampArray(lampArrayPtr);

                int lampCount = lampArray.getLampCount();
                LampArrayInterop.LampArrayPosition boundingBox = lampArray.getBoundingBox();
                System.out.println("LampArray Callback: count=" + s_lampArrays.size()
                        + ", kind=" + lampArray.getKind()
                        + ", lampCount=" + lampCount
                        + ", vendorId=0x" + Integer.toHexString(lampArray.getVendorId())
                        + ", productId=0x" + Integer.toHexString(lampArray.getProductId())
                        + ", currentStatus=" + currentStatus
                        + ", previousStatus=" + previousStatus
                        + ", dimensions=[x=" + boundingBox.xInMeters
                        + ", y=" + boundingBox.yInMeters
                        + ", z=" + boundingBox.zInMeters + "]");

                /*
                int[] indices = new int[lampCount];
                int[] scanCodes = new int[lampCount];

                LampArrayInterop.LampArrayColor colors[] = (LampArrayInterop.LampArrayColor[]) new LampArrayInterop.LampArrayColor().toArray(lampCount);

                for (int i = 0; i < lampCount; i++)
                {
                    LampArrayInterop.ILampInfo lampInfo = lampArray.getLampInfo(i);
                    indices[i] = i;

                    if ((i % 2) == 0)
                    {
                        // scanCodes[i] = lampInfo.getScanCode();
                        colors[i].set(LampArrayColorConstants.blue);
                    }
                    else
                    {
                        // scanCodes[i] = KeyboardScanCode.SC_INVALID.getCode();
                        colors[i].set(LampArrayColorConstants.red);
                    }
                }

                lampArray.setColorsForIndices(indices, colors);
                // lampArray.setColorsForScanCodes(scanCodes, colors);
                 // */

                LampArrayEffectContext foundLampArray = null;
                for (LampArrayEffectContext attachedLampArray : s_lampArrays)
                {
                    var myPointer = attachedLampArray.lampArray.getPointer();
                    if (myPointer.equals(lampArrayPtr))
                    {
                        foundLampArray = attachedLampArray;
                        break;
                    }
                }

                if (isConnected && (foundLampArray == null))
                {
                    System.out.println("LampArray Added: kind=" + lampArray.getKind() + ", lampCount=" + lampArray.getLampCount());

                    var newContext = new LampArrayEffectContext();
                    newContext.lampArray = lampArray;

                    lampArray.AddRef();
                    s_lampArrays.add(newContext);

                    scheduleEffectUpdate(0);
                }
                else if (!isConnected && (foundLampArray != null))
                {
                    System.out.println("LampArray Removed: kind=" + lampArray.getKind() + ", lampCount=" + lampArray.getLampCount());

                    s_lampArrays.remove(foundLampArray);
                    lampArray.Release();

                    scheduleEffectUpdate(0);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
            finally
            {
                s_lampArraysLock.unlock();
            }
        }
    };

    public LampArrayDeviceManager()
    {
        // Register LampArrayStatusCallback
        int hr = LampArrayInterop.INSTANCE.RegisterLampArrayStatusCallback(
            OnLampArrayStatusChanged,
            LampArrayInterop.LampArrayEnumerationKind.Async.ordinal(),
            null,
            s_callbackToken);

        System.out.println("Callback registered! hr=" + Integer.toHexString(hr) + ", Token: " + s_callbackToken.toString());
    }

    @Override
    public void close() throws Exception
    {
        LampArrayInterop.INSTANCE.UnregisterLampArrayCallback(
            s_callbackToken.getValue(),
            1000);
    }

    // Returns the number of milliseconds to wait before performing the next effect update.
    public static void updateEffects()
    {
        System.out.println("UpdateEffects loop");

        long nextDueTime = Long.MAX_VALUE;

        try
        {
            s_lampArraysLock.lock();
            long currentTimeMilliseconds = System.currentTimeMillis();
            System.out.println("Current time loop start: " + currentTimeMilliseconds);

            if (s_effectTypeDirty)
            {
                // TODO: Update current effect based on this
                MinecraftEffectState.EffectType effectType = s_effectType;
                s_effectTypeDirty = false;
            }

            Iterator<LampArrayEffectContext> effectContextIterator = s_lampArrays.iterator();
            while (effectContextIterator.hasNext())
            {
                // TODO: Create the effect for the current game mode
                LampArrayEffectContext effectContext = effectContextIterator.next();
                if (effectContext.effect == null)
                {
                    int[] indices = new int[effectContext.lampArray.getLampCount()];
                    LampArrayInterop.LampArrayColor[] colors = (LampArrayInterop.LampArrayColor[]) new LampArrayInterop.LampArrayColor().toArray(indices.length);
                    for (int i = 0; i < colors.length; i++)
                    {
                        indices[i] = i;
                        if ((i % 2) == 0)
                        {
                            colors[i].set(LampArrayColorConstants.blue);
                        }
                        else
                        {
                            colors[i].set(LampArrayColorConstants.red);
                        }
                    }

                    effectContext.effect = s_effectFactory.CreateEffect(
                        MinecraftEffectState.EffectType.Idle,
                        effectContext.lampArray);

                    effectContext.effect = new StaticEffect(
                        effectContext.lampArray,
                        colors,
                        indices,
                        5000);
                }

                long dueTime = effectContext.effect.updateLamps(currentTimeMilliseconds);
                if (dueTime < nextDueTime)
                {
                    nextDueTime = dueTime;
                }

                try
                {
                    effectContext.effect.sendLampUpdate();
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                }

                if (effectContext.effect.isCompleted())
                {
                    // Remove it from the list
                    System.out.println("Completed effect being removed.");
                    effectContext.effect.stop();
                    effectContext.effect = null;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        finally
        {
            long postLoopIterationTime = System.currentTimeMillis();
            System.out.println("Post loop time ms: " + postLoopIterationTime);
            System.out.println("Next due time ms: " + nextDueTime);

            long schedulerInterval = nextDueTime - postLoopIterationTime;
            if (schedulerInterval < 0)
            {
                schedulerInterval = 0;
            }

            scheduleEffectUpdate(schedulerInterval);
            s_lampArraysLock.unlock();
        }
    }

    private static void scheduleEffectUpdate(long schedulerDelayMilliseconds)
    {
        // Schedule our next update
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                updateEffects();
            }
        };

        if (s_scheduledTask != null)
        {
            s_scheduledTask.cancel(false);
            s_scheduledTask = null;
        }

        System.out.println("Scheduling next loop in " + schedulerDelayMilliseconds + " ms.");
        s_scheduler.schedule(runnable, schedulerDelayMilliseconds, TimeUnit.MILLISECONDS);
    }


    public void updateEffectType(MinecraftEffectState.EffectType effectType)
    {
        s_effectType = effectType;
        s_effectTypeDirty = true;

        scheduleEffectUpdate(0);
    }
}
