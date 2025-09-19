package com.tyduckmsft.lamparraymod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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

    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, MOD_ID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .setId(BLOCKS.key("example_block"))
                    .mapColor(MapColor.STONE)
            )
    );
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block",
            () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().setId(ITEMS.key("example_block")))
    );

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new Item(new Item.Properties()
                    .setId(ITEMS.key("example_item"))
                    .food(new FoodProperties.Builder()
                            .alwaysEdible()
                            .nutrition(1)
                            .saturationModifier(2f)
                            .build()
                    )
            )
    );

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    // Helper class that will interop with the native LampArray API
    private static LampArrayDeviceManager m_deviceManager;

    private class GameState
    {
        public boolean playerInWater = false;
    };

    private GameState m_gameState = new GameState();

    public LampArrayMod(FMLJavaModLoadingContext context)
    {
        // m_deviceManager = new LampArrayDeviceManager();

        var modBusGroup = context.getModBusGroup();

        // Register the commonSetup method for modloading
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modBusGroup);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modBusGroup);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modBusGroup);

        // Register the item to a creative tab
        BuildCreativeModeTabContentsEvent.getBus(modBusGroup).addListener(LampArrayMod::addCreative);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    private void commonSetup(FMLCommonSetupEvent fmlCommonSetupEvent)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    @SubscribeEvent
    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    @SubscribeEvent
    public void onWorldLoaded(LevelEvent.Load event)
    {
        System.out.println("World Loaded!");
        // m_deviceManager.updateEffectType(MinecraftEffectState.EffectType.Biome);
    }

    @SubscribeEvent
    public void onWorldUnloaded(LevelEvent.Unload event)
    {
        System.out.println("World Unloaded!");
        // m_deviceManager.updateEffectType(MinecraftEffectState.EffectType.Idle);
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
            // m_deviceManager.updateEffectType(MinecraftEffectState.EffectType.Underwater);
        }
        else if (!event.player.isInWater() && m_gameState.playerInWater)
        {
            System.out.println("Left the water.");
            // m_deviceManager.updateEffectType(MinecraftEffectState.EffectType.Biome);
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
