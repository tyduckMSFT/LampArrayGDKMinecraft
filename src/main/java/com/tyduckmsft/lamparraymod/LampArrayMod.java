package com.tyduckmsft.lamparraymod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LampArrayMod.MOD_ID)
public final class LampArrayMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "lamparraymod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private final LampArrayDeviceManager m_deviceManager = new LampArrayDeviceManager();

    private class GameState
    {
        public boolean playerInWater = false;
    };

    private final GameState m_gameState = new GameState();

    public LampArrayMod(FMLJavaModLoadingContext context)
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the commonSetup method for modloading
        var modBusGroup = context.getModBusGroup();
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);

        // Register the item to a creative tab
        BuildCreativeModeTabContentsEvent.getBus(modBusGroup).addListener(LampArrayMod::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    private static void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }


    @SubscribeEvent
    public void onWorldLoaded(LevelEvent.Load event)
    {
        System.out.println("World Loaded!");
        m_deviceManager.updateEffectType(MinecraftLightingEffectState.Biome);
    }

    @SubscribeEvent
    public void onWorldUnloaded(LevelEvent.Unload event) {
        System.out.println("World Unloaded!");
        m_deviceManager.updateEffectType(MinecraftLightingEffectState.Idle);
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event)
    {
        var player = Minecraft.getInstance().player;
        if ((player != null) && (event.getEntity().getName().equals(player.getName())))
        {
            System.out.println("Ouch! That hurt me!");
        }
    }

    @SubscribeEvent
    public void onLivingDead(LivingDeathEvent event)
    {
        var player = Minecraft.getInstance().player;
        if ((player != null) && (event.getEntity().getName().equals(player.getName())))
        {
            System.out.println("Player died.");
        }
    }

    @SubscribeEvent
    public void handlePlayerTickEvent(TickEvent.PlayerTickEvent event) throws IOException
    {
        if (event.player.isInWater())
        {
            if (!m_gameState.playerInWater)
            {
                m_gameState.playerInWater = true;
                System.out.println("Entered the water.");
                m_deviceManager.updateEffectType(MinecraftLightingEffectState.Underwater);
            }
        }
        else
        {
            if (m_gameState.playerInWater)
            {
                m_gameState.playerInWater = false;
                System.out.println("Left the water.");
                m_deviceManager.updateEffectType(MinecraftLightingEffectState.Biome);
            }
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            System.out.println("Hello from tyduck!");
        }
    }
}
