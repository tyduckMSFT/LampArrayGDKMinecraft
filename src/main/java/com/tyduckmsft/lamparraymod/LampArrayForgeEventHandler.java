package com.tyduckmsft.lamparraymod;

import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.client.telemetry.events.WorldUnloadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public class LampArrayForgeEventHandler
{
    private LampArrayDeviceManager m_manager;

    public LampArrayForgeEventHandler(LampArrayDeviceManager manager)
    {
        m_manager = manager;
    }

    public void register()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    /*
    @SubscribeEvent
    public void onWorldLoaded(World)
    {
        System.out.println("World loaded.");
    }

    @SubscribeEvent
    public void onWorldUnloaded(WorldUnloadEvent event)
    {
        System.out.println("World unloaded.");
    }

     */
}
