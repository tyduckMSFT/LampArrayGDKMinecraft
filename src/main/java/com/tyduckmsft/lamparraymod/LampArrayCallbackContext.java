package com.tyduckmsft.lamparraymod;

import com.sun.jna.Structure;
import com.sun.jna.ptr.LongByReference;

import java.util.ArrayList;
import java.util.List;

public class LampArrayCallbackContext extends Structure
{
    public LongByReference m_callbackToken = new LongByReference();

    public List<LampArrayInterop.ILampArray> m_lampArrays = new ArrayList<LampArrayInterop.ILampArray>();
}
