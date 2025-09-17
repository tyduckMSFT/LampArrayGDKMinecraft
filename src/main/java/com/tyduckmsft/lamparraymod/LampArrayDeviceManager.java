package com.tyduckmsft.lamparraymod;

import com.sun.jna.*;
import com.sun.jna.ptr.*;

import java.util.ArrayList;
import java.util.List;

public class LampArrayDeviceManager extends Structure
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
                System.loadLibrary("LampArray.dll");

                System.out.println("LampArray DLL loaded!");

                // Load the effects helper library DLL, if it exists.
                // If it's not present, lighting can still be controlled with just LampArray.
                // System.loadLibrary("LampArrayEffects.dll");
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

    private static LampArrayCallbackContext s_callbackContext = new LampArrayCallbackContext();

    private static LampArrayInterop.LampArrayStatusCallback OnLampArrayStatusChanged = new LampArrayInterop.LampArrayStatusCallback()
    {
        @Override
        public void invoke(Pointer context, int currentStatus, int previousStatus, Pointer lampArrayPtr)
        {
            try
            {
                boolean wasConnected = (previousStatus & LampArrayInterop.LampArrayStatus.Connected.value) != 0;
                boolean isConnected = (currentStatus & LampArrayInterop.LampArrayStatus.Connected.value) != 0;

                LampArrayInterop.ILampArray lampArray = new LampArrayInterop.ILampArray(lampArrayPtr);

                if (wasConnected != isConnected)
                {
                    if (isConnected)
                    {
                        s_callbackContext.m_lampArrays.add(lampArray);

                        LampArrayInterop.LampArrayColor redColor = new LampArrayInterop.LampArrayColor();
                        lampArray.setColor(redColor);
                    }
                    else
                    {
                        s_callbackContext.m_lampArrays.remove(lampArray);
                    }
                }
            }
            catch (Exception ignored) {}
        }
    };

    public LampArrayDeviceManager()
    {
        // Register LampArrayStatusCallback
        m_lampArrayInterop.RegisterLampArrayStatusCallback(
            OnLampArrayStatusChanged,
            LampArrayInterop.LampArrayEnumerationKind.Async.ordinal(),
            s_callbackContext.getPointer(),
            s_callbackContext.m_callbackToken);
    }

    public void Teardown() throws Exception
    {
        m_lampArrayInterop.UnregisterLampArrayCallback(
                s_callbackContext.m_callbackToken.getValue(),
                1000);
    }
}
