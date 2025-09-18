package com.tyduckmsft.lamparraymod;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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

    private final LampArrayInterop m_lampArrayInterop = LampArrayInterop.INSTANCE;

    private static LongByReference s_callbackToken = new LongByReference();

    private static Lock s_lampArraysLock = new ReentrantLock();
    private static List<LampArrayInterop.ILampArray> s_lampArrays = new ArrayList<LampArrayInterop.ILampArray>();

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
                Integer[] indices = new Integer[lampCount];
                LampArrayInterop.LampArrayColor colors[] = (LampArrayInterop.LampArrayColor[]) new LampArrayInterop.LampArrayColor().toArray(lampCount);

                for (int i = 0; i < lampCount; i++)
                {
                    indices[i] = i;
                    colors[i] = redColor;
                }
                *?
                 */

                LampArrayInterop.ILampArray foundLampArray = null;
                for (LampArrayInterop.ILampArray attachedLampArray : s_lampArrays)
                {
                    var myPointer = attachedLampArray.getPointer();
                    if (myPointer.equals(lampArrayPtr))
                    {
                        foundLampArray = attachedLampArray;
                        break;
                    }
                }

                if (isConnected && (foundLampArray == null))
                {
                    System.out.println("LampArray Added: kind=" + lampArray.getKind() + ", lampCount=" + lampArray.getLampCount());

                    lampArray.AddRef();
                    s_lampArrays.add(lampArray);

                    lampArray.setColor(ColorConstants.minecraftGreen);
                }
                else if (!isConnected && (foundLampArray != null))
                {
                    System.out.println("LampArray Removed: kind=" + lampArray.getKind() + ", lampCount=" + lampArray.getLampCount());

                    s_lampArrays.remove(foundLampArray);
                    lampArray.Release();
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
        int hr = m_lampArrayInterop.RegisterLampArrayStatusCallback(
            OnLampArrayStatusChanged,
            LampArrayInterop.LampArrayEnumerationKind.Async.ordinal(),
            null,
            s_callbackToken);

        System.out.println("Callback registered! hr=" + Integer.toHexString(hr) + ", Token: " + s_callbackToken.toString());
    }

    @Override
    public void close() throws Exception
    {
        m_lampArrayInterop.UnregisterLampArrayCallback(
            s_callbackToken.getValue(),
            1000);
    }
}
