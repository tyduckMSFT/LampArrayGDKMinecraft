package com.tyduckmsft.lamparraymod;

import net.minecraftforge.common.MinecraftForge;

public class LampArrayForgeEventHandler {

    public void register()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
