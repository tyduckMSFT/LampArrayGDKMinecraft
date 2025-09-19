package com.tyduckmsft.lamparraymod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LampArrayMod.MOD_ID)
public final class LampArrayMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "lamparraymod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Helper class that will interop with the native LampArray API
    private static final LampArrayDeviceManager m_deviceManager = new LampArrayDeviceManager();

    private class GameState
    {
        public boolean playerInWater = false;
    };

    private GameState m_gameState = new GameState();
//    public static final RegistryObject<Bio>

    public LampArrayMod(FMLJavaModLoadingContext context)
    {
        var modBusGroup = context.getModBusGroup();

        // Register the commonSetup method for modloading
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

//    @SubscribeEvent
//    public void mainMenuEvent()

    @SubscribeEvent
    public void onWorldLoaded(LevelEvent.Load event)
    {
        System.out.println("World Loaded!");
    }

    @SubscribeEvent
    public void onWorldUnloaded(LevelEvent.Unload event)
    {
        System.out.println("World Unloaded!");
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
        if (event.player.isInWater() && !m_gameState.playerInWater)
        {
            System.out.println("Entered the water.");
            // TODO: Send event
        }
        else if (!event.player.isInWater() && m_gameState.playerInWater)
        {
            System.out.println("Left the water.");
            // TODO: Send event
        }

        var position = event.player.blockPosition();
        try (Level level = event.player.level())
        {
            long time = level.getDayTime();

            Holder<Biome> biomeHolder = level.getBiome(position);
            Biome biomeValue = biomeHolder.value();
            var biomeType = biomeValue.modifiableBiomeInfo().getOriginalBiomeInfo();
        }
    }

//    @SubscribeEvent
//    private static void onBiomeLoading(Biome event) {}


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
