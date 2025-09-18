package com.tyduckmsft.lamparraymod;

import java.util.*;

import com.sun.jna.*;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.*;
import org.checkerframework.checker.units.qual.A;

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
    class LampArrayColor extends Structure
    {
        public byte r, g, b, a;

        LampArrayColor() {}

        LampArrayColor(byte r, byte g, byte b, byte a)
        {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        LampArrayColor(LampArrayColor otherColor)
        {
            this.r = otherColor.r;
            this.g = otherColor.g;
            this.b = otherColor.b;
            this.a = otherColor.a;
        }

        @Override
        protected List<String> getFieldOrder() {
            return List.of("r", "g", "b", "a");
        }

        public static class ByReference extends LampArrayColor implements Structure.ByReference
        {
            ByReference(LampArrayColor color)
            {
                super(color);
            }
        }

        public static class ByValue extends LampArrayColor implements Structure.ByValue
        {
            ByValue(LampArrayColor color)
            {
                super(color);
            }
        }
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
        protected int callIntMethod(int vtblIndex, Object... args)
        {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            return (Integer) func.invoke(Integer.class, fullArgs);
        }

        protected short callShortMethod(int vtblIndex, Object... args)
        {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            return (Short) func.invoke(Short.class, fullArgs);
        }

        protected long callLongMethod(int vtblIndex, Object... args)
        {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            return (Long) func.invoke(Long.class, fullArgs);
        }

        protected double callDoubleMethod(int vtblIndex, Object... args)
        {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            return (Double) func.invoke(Double.class, fullArgs);
        }

        protected boolean callBooleanMethod(int vtblIndex, Object... args)
        {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            return (Boolean) func.invoke(Boolean.class, fullArgs);
        }

        protected void callVoidMethod(int vtblIndex, Object... args)
        {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(vtblIndex * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[args.length + 1];
            fullArgs[0] = getPointer();
            System.arraycopy(args, 0, fullArgs, 1, args.length);
            func.invoke(Void.class, fullArgs);
        }
    }

    public class ILampInfo extends UnknownHelper
    {
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

        public LampArrayPosition getPosition()
        {
            LampArrayPosition position = new LampArrayPosition();
            callVoidMethod(VTBL_GET_POSITION, position);
            return position;
        }

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

        public LampArrayColor getFixedColor()
        {
            LampArrayColor fixedColor = new LampArrayColor();
            if (callBooleanMethod(VTBL_GET_FIXED_COLOR, fixedColor))
            {
                return fixedColor;
            }

            return null;
        }

        public LampArrayColor getNearestSupportedColor(LampArrayColor desiredColor)
        {
            Pointer vTable = getPointer().getPointer(0);
            Function func = Function.getFunction(vTable.getPointer(VTBL_GET_NEAREST_SUPPORTED_COLOR * Native.POINTER_SIZE), Function.ALT_CONVENTION);
            Object[] fullArgs = new Object[2];
            fullArgs[0] = getPointer();
            fullArgs[1] = new LampArrayColor.ByValue(desiredColor);
            return (LampArrayColor) func.invoke(LampArrayColor.class, fullArgs);
        }

        public int getUpdateLatency()
        {
            return callIntMethod(VTBL_GET_UPDATE_LATENCY);
        }

        public int getScanCode()
        {
            return callIntMethod(VTBL_GET_SCAN_CODE);
        }
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

        public LampArrayPosition getBoundingBox()
        {
            LampArrayPosition boundingBox = new LampArrayPosition();
            callVoidMethod(VTBL_GET_BOUNDING_BOX, boundingBox);
            return boundingBox;
        }

        public boolean getIsEnabled()
        {
            return callBooleanMethod(VTBL_GET_IS_ENABLED);
        }

        public double getBrightnessLevel()
        {
            return callDoubleMethod(VTBL_GET_BRIGHTNESS);
        }

        public void setBrightnessLevel(double brightnessLevel)
        {
            callVoidMethod(VTBL_SET_BRIGHTNESS, brightnessLevel);
        }

        public void setIsEnabled(boolean enabled)
        {
            callVoidMethod(VTBL_SET_IS_ENABLED, enabled);
        }

        public boolean supportsScanCodes()
        {
            return callBooleanMethod(VTBL_SUPPORTS_SCAN_CODES);
        }

        public ILampInfo getLampInfo(int index)
        {
            PointerByReference ref = new PointerByReference();
            if (callIntMethod(VTBL_GET_LAMP_INFO, index, ref) == 0)
            {
                return new ILampInfo(ref.getValue());
            }
            return null;
        }

        public List<Integer> getIndicesForPurposes(LampPurposes purposes)
        {
            int arraySize = callIntMethod(VTBL_GET_INDICES_COUNT_FOR_PURPOSES, purposes.value);
            if (arraySize > 0)
            {
                int[] indicesArray = new int[arraySize];
                callVoidMethod(VTBL_GET_INDICES_FOR_PURPOSES, purposes.value, indicesArray.length, indicesArray);
                return Arrays.stream(indicesArray).boxed().toList();
            }

            return new ArrayList<Integer>();
        }

        public void setColor(LampArrayColor color)
        {
            callVoidMethod(VTBL_SET_COLOR, new LampArrayColor.ByValue(color));
        }

        public void setColorsForIndices(
            int[] indices,
            LampArrayColor[] colors) throws IllegalArgumentException
        {
            if (indices.length == colors.length)
            {
                callVoidMethod(VTBL_SET_COLORS_FOR_INDICES, indices.length, indices, colors);
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }

        public void setColorsForScanCodes(
            int[] scanCodes,
            LampArrayColor[] colors) throws IllegalArgumentException
        {
            if (scanCodes.length == colors.length)
            {
                callVoidMethod(VTBL_SET_COLORS_FOR_SCAN_CODES, scanCodes.length, scanCodes, colors);
            }
            else
            {
                throw new IllegalArgumentException();
            }
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
    interface LampArrayCallback extends Callback
    {
        void invoke(Pointer context, boolean isAttached, Pointer lampArray);
    }

    interface LampArrayStatusCallback extends Callback
    {
        void invoke(Pointer context, int currentStatus, int previousStatus, Pointer lampArray);
    }

    // Native methods
    boolean UnregisterLampArrayCallback(long callbackToken, long timeoutInMicroseconds);

    boolean TrySetLampArrayWorkerThreadAffinityMask(long threadAffinityMask);

    int RegisterLampArrayCallback(
        LampArrayCallback callback,
        Pointer context,
        LongByReference callbackToken);

    int RegisterLampArrayStatusCallback(
        LampArrayStatusCallback callbackFunc,
        int enumerationKind,
        Pointer context,
        LongByReference callbackToken);
}
