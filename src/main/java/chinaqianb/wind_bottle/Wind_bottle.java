package chinaqianb.wind_bottle;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Wind_bottle.MODID)
public class Wind_bottle {

    public static final String MODID = "wind_bottle";

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Items ITEMS =DeferredRegister.createItems(MODID);
    public static final Supplier<Item> wind_bottle =ITEMS.register("wind_bottle",()->new Item(new Item.Properties()));

    public Wind_bottle(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);
        ITEMS.register(modEventBus);
        modEventBus.addListener(this::ItemGroup);
        NeoForge.EVENT_BUS.register(this);





//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }




    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {


    }
    public  void ItemGroup(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey()== CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(Wind_bottle.wind_bottle.get());
        }

    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
