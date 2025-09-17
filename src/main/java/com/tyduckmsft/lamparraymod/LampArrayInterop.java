package com.tyduckmsft.lamparraymod;

import java.util.*;

import com.sun.jna.*;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.*;

public interface LampArrayInterop extends Library {
    LampArrayInterop INSTANCE = Native.load("LampArray.dll", LampArrayInterop.class);

    // Enums as constants
    public enum LampPurposes
    {
        Undefined(0x00),
        Control(0x01),
        Accent(0x02),
        Branding(0x04),
        Status(0x08),
        Illumination(0x10),
        Presentation(0x20);

        public final int value;
        LampPurposes(int value) { this.value = value; }
    }

    public enum LampArrayKind
    {
        Undefined(0),
        Keyboard(1),
        Mouse(2),
        GameController(3),
        Peripheral(4),
        Scene(5),
        Notification(6),
        Chassis(7),
        Wearable(8),
        Furniture(9),
        Art(10),
        Headset(11),
        Microphone(12),
        Speaker(13);

        public final int value;
        LampArrayKind(int value) { this.value = value; }
    };

    public enum LampArrayEnumerationKind
    {
        Async(1),
        Blocking(2);

        public final int value;
        LampArrayEnumerationKind(int value) { this.value = value; }
    }

    public enum LampArrayStatus
    {
        None(0x00000000),
        Connected(0x00000001),
        Available(0x00000002);

        public final int value;
        LampArrayStatus(int value) { this.value = value; }
    }

    // Structs
    class LampArrayColor extends Structure {
        public byte r, g, b, a;

        @Override
        protected List<String> getFieldOrder() {
            return List.of("r", "g", "b", "a");
        }

        public static class ByReference extends LampArrayColor implements Structure.ByReference {}
    }

    class LampArrayPosition extends Structure {
        public float xInMeters, yInMeters, zInMeters;

        @Override
        protected List<String> getFieldOrder() {
            return List.of("xInMeters", "yInMeters", "zInMeters");
        }

        public static class ByReference extends LampArrayPosition implements Structure.ByReference {}
    }

    // Interfaces
    public class UnknownHelper extends Unknown
    {
        UnknownHelper(Pointer pointer)
        {
            super(pointer);
        }

        // Helper methods
        protected int callIntMethod(int vtblIndex, Object... args) {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            return (Integer) func.invoke(Integer.class, fullArgs);
        }

        protected short callShortMethod(int vtblIndex) {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            return (Short) func.invoke(Short.class, new Object[]{getPointer()});
        }

        protected long callLongMethod(int vtblIndex) {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            return (Long) func.invoke(Long.class, new Object[]{getPointer()});
        }

        protected boolean callBooleanMethod(int vtblIndex) {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            return (Boolean) func.invoke(Boolean.class, new Object[]{getPointer()});
        }

        protected void callVoidMethod(int vtblIndex, Object... args) {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            func.invoke(Void.class, fullArgs);
        }
    }

    public class ILampInfo extends UnknownHelper {
        Guid.GUID IID_ILampInfo = new Guid.GUID("AE82EDD1-6B75-43CF-9BEA-F600A49A320C");

        // Method index constants (based on vtable layout)
        int VTBL_GET_INDEX = 3;
        int VTBL_GET_PURPOSES = 4;
        int VTBL_GET_POSITION = 5;
        int VTBL_GET_RED_LEVEL_COUNT = 6;
        int VTBL_GET_GREEN_LEVEL_COUNT = 7;
        int VTBL_GET_BLUE_LEVEL_COUNT = 8;
        int VTBL_GET_GAIN_LEVEL_COUNT = 9;
        int VTBL_GET_FIXED_COLOR = 10;
        int VTBL_GET_NEAREST_SUPPORTED_COLOR = 11;
        int VTBL_GET_UPDATE_LATENCY = 12;
        int VTBL_GET_SCAN_CODE = 13;

        public ILampInfo(Pointer pointer)
        {
            super(pointer);
        }

        public int getIndex()
        {
            return callIntMethod(VTBL_GET_INDEX);
        }

        public int getPurposes()
        {
            return callIntMethod(VTBL_GET_PURPOSES);
        }

        // TODO

        public int getRedLevelCount()
        {
            return callIntMethod(VTBL_GET_RED_LEVEL_COUNT);
        }

        public int getGreenLevelCount()
        {
            return callIntMethod(VTBL_GET_GREEN_LEVEL_COUNT);
        }

        public int getBlueLevelCount()
        {
            return callIntMethod(VTBL_GET_BLUE_LEVEL_COUNT);
        }

        public int getGainLevelCount()
        {
            return callIntMethod(VTBL_GET_GAIN_LEVEL_COUNT);
        }

        // TODO

        public int getUpdateLatency()
        {
            return callIntMethod(VTBL_GET_UPDATE_LATENCY);
        }

        public int getScanCode()
        {
            return callIntMethod(VTBL_GET_SCAN_CODE);
        }

        // TODO: Fill in more
    }

    public class ILampArray extends UnknownHelper
    {
        Guid.GUID IID_ILampArray = new Guid.GUID("45577878-466D-40ED-AB72-C448C70E1252");

        public ILampArray(Pointer pointer)
        {
            super(pointer);
        }

        // VTable index constants
        int VTBL_GET_DEVICE_ID = 3;
        int VTBL_GET_VENDOR_ID = 4;
        int VTBL_GET_PRODUCT_ID = 5;
        int VTBL_GET_HARDWARE_VERSION = 6;
        int VTBL_GET_KIND = 7;
        int VTBL_GET_LAMP_COUNT = 8;
        int VTBL_GET_MIN_UPDATE_INTERVAL = 9;
        int VTBL_GET_BOUNDING_BOX = 10;
        int VTBL_GET_IS_ENABLED = 11;
        int VTBL_SET_IS_ENABLED = 12;
        int VTBL_GET_BRIGHTNESS = 13;
        int VTBL_SET_BRIGHTNESS = 14;
        int VTBL_SUPPORTS_SCAN_CODES = 15;
        int VTBL_GET_LAMP_INFO = 16;
        int VTBL_GET_INDICES_COUNT_FOR_PURPOSES = 17;
        int VTBL_GET_INDICES_FOR_PURPOSES = 18;
        int VTBL_SET_COLOR = 19;
        int VTBL_SET_COLORS_FOR_INDICES = 20;
        int VTBL_SET_COLORS_FOR_SCAN_CODES = 21;

        // Method wrappers
        public short getVendorId()
        {
            return callShortMethod(VTBL_GET_VENDOR_ID);
        }

        public short getProductId()
        {
            return callShortMethod(VTBL_GET_PRODUCT_ID);
        }

        public short getHardwareVersion()
        {
            return callShortMethod(VTBL_GET_HARDWARE_VERSION);
        }

        public LampArrayKind getKind()
        {
            return LampArrayKind.values()[callIntMethod(VTBL_GET_KIND)];
        }

        public int getLampCount()
        {
            return callIntMethod(VTBL_GET_LAMP_COUNT);
        }

        public long getMinUpdateIntervalInMicroseconds()
        {
            return callLongMethod(VTBL_GET_MIN_UPDATE_INTERVAL);
        }

        public boolean getIsEnabled()
        {
            return callBooleanMethod(VTBL_GET_IS_ENABLED);
        }

        public void setIsEnabled(boolean enabled)
        {
            callVoidMethod(VTBL_SET_IS_ENABLED, enabled);
        }

        public boolean supportsScanCodes()
        {
            return callBooleanMethod(VTBL_SUPPORTS_SCAN_CODES);
        }

        public int getIndicesCountForPurposes(LampPurposes purposes)
        {
            return callIntMethod(VTBL_GET_INDICES_COUNT_FOR_PURPOSES, purposes);
        }

        public void setColor(LampArrayColor color)
        {
            callVoidMethod(VTBL_SET_COLOR, color);
        }

        /*
                int VTBL_GET_BRIGHTNESS = 13;
        int VTBL_SET_BRIGHTNESS = 14;
        int VTBL_SUPPORTS_SCAN_CODES = 15;
        int VTBL_GET_LAMP_INFO = 16;
        int VTBL_GET_INDICES_COUNT_FOR_PURPOSES = 17;
        int VTBL_GET_INDICES_FOR_PURPOSES = 18;
        int VTBL_SET_COLOR = 19;
        int VTBL_SET_COLORS_FOR_INDICES = 20;
        int VTBL_SET_COLORS_FOR_SCAN_CODES = 21;
         */
    }

    // Callback interfaces
    interface LampArrayCallback extends Callback {
        void invoke(Pointer context, boolean isAttached, Pointer lampArray);
    }

    interface LampArrayStatusCallback extends Callback {
        void invoke(Pointer context, int currentStatus, int previousStatus, Pointer lampArray);
    }

    // Native methods
    boolean UnregisterLampArrayCallback(long callbackToken, long timeoutInMicroseconds);

    boolean TrySetLampArrayWorkerThreadAffinityMask(long threadAffinityMask);

    int RegisterLampArrayCallback(
        LampArrayCallback callback,
        Pointer context,
        LongByReference callbackToken
    );

    int RegisterLampArrayStatusCallback(
        LampArrayStatusCallback callbackFunc,
        int enumerationKind,
        Pointer context,
        LongByReference callbackToken
    );
}
