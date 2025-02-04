package chinaqianb.wind_bottle;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.font.providers.UnihexProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.DimensionDefaults;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import java.util.List;

@EventBusSubscriber(modid = Wind_bottle.MODID)
public class ModEvent {
    private static final Logger LOGGER = LogUtils.getLogger();
    static int tick =0;
    @SubscribeEvent
    public static void PlayerTickEvent(PlayerTickEvent.Post event){
        Player player =event.getEntity();
        Level world =player.level();
        ItemStack item= player.getMainHandItem();
        if (world instanceof ServerLevel){

        if (item.getItem()==Items.GLASS_BOTTLE && player.getKnownMovement().length()>=1) {
            tick++;

            if (tick == 100) {
                player.setItemInHand(player.getUsedItemHand(), new ItemStack(Wind_bottle.wind_bottle.get(), item.getCount()));
                play_sound(world, player);
                tick = 0;

            }
        }
        }

    }
    @SubscribeEvent
    public static void ServerTickEvent(EntityTickEvent.Post event){
             Entity entity=event.getEntity();
           Level world= entity.level();

            if (world instanceof ServerLevel){
            if (entity instanceof ItemEntity) {

                ItemEntity itemEntity = (ItemEntity) entity;
                BlockPos pos = new BlockPos((int) itemEntity.getX(), (int) itemEntity.getY(), (int) itemEntity.getZ());
                if (itemEntity.getItem().getItem() == Wind_bottle.wind_bottle.get()) {
                    if (world.getBrightness(LightLayer.BLOCK,pos)>=10 || itemEntity.isInWater() || world.getBiome(pos).is(Biomes.DESERT) || world.dimension() == Level.NETHER) {
                        tick++;
                        if (tick == 50) {
                            for (int i = 0; i < itemEntity.getItem().getCount(); i++) {
                                itemEntityHander(world, itemEntity);
                            }
                            itemEntity.kill();
                            tick = 0;
                        }


                    } else if (world.isDay()) {
                    tick++;
                    if (tick == 300) {
                        for (int i = 0; i < itemEntity.getItem().getCount(); i++) {
                            itemEntityHander(world, itemEntity);
                        }
                        itemEntity.kill();
                        tick = 0;
                    }
                }

                } else if (itemEntity.getItem().getItem() == Items.GLASS_BOTTLE) {
                    if (itemEntity.getKnownMovement().length() >= 1) {
                       tick++;
                       if (tick==100){
                           itemEntity.setItem(Wind_bottle.wind_bottle.get().getDefaultInstance());
                           world.playSound(itemEntity,pos,SoundEvents.BOTTLE_FILL,SoundSource.NEUTRAL,2.0f,1.5f);
                           tick=0;
                       }
                    }
                }
            } else if (entity.getType() == EntityType.FOX) {
                Fox fox = (Fox) entity;
                if (fox.getMainHandItem().getItem() == Wind_bottle.wind_bottle.get()) {
                    tick++;
                    if (tick == 100) {
                        itemEntityHander(world, fox);
                        fox.setItemInHand(fox.getUsedItemHand(), ItemStack.EMPTY);
                        tick = 0;
                    }
                }
            } else if (entity instanceof ItemFrame) {
                ItemFrame frame = (ItemFrame) entity;
                if (frame.getItem().getItem() == Wind_bottle.wind_bottle.get()) {
                    itemEntityHander(world, frame);
                    frame.setItem(ItemStack.EMPTY);
                }

            }
        }

    }
    @SubscribeEvent
    public static void UseEntityBack(PlayerInteractEvent.EntityInteract event){
        boolean result =false;
        Entity entity =event.getTarget();
        Player player =event.getEntity();
        Level world =event.getLevel();
        ItemStack stack =player.getItemInHand(event.getHand());
        if (stack.getItem()== Items.GLASS_BOTTLE){
            if (entity.getType() == EntityType.BREEZE ||entity.getType() ==EntityType.STRAY){
                if (!world.isClientSide()){
                    stack.shrink(1);
                    player.addItem(Wind_bottle.wind_bottle.get().getDefaultInstance());
                }else {
                    play_sound(world,player);
                }
               result=true;
            } else if (entity.getType() ==EntityType.WIND_CHARGE ||entity.getType() ==EntityType.BREEZE_WIND_CHARGE) {
                if (!world.isClientSide()){
                    stack.shrink(1);
                    entity.kill();
                    player.addItem(Wind_bottle.wind_bottle.get().getDefaultInstance());
                }else {
                    play_sound(world,player);
                }
               result=true;
            }
        }
        event.setCancellationResult(backResult(result));
    }


    public static void play_sound(Level level,Player player){
       level.playSound(player,player.getX(),player.getY(),player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL,2.0F,1.5F);
    }
    public static InteractionResult backResult(boolean result){
        return result ? InteractionResult.SUCCESS :InteractionResult.PASS;
    }
    private static <T extends Entity> void itemEntityHander(Level world, T itemEntity){
        world.addFreshEntity(new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), Items.GLASS_BOTTLE.getDefaultInstance()));
        world.addFreshEntity(new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), Items.WIND_CHARGE.getDefaultInstance()));
        BlockPos pos =new BlockPos((int) itemEntity.getX(), (int) itemEntity.getY(), (int) itemEntity.getZ());
        world.playSound(itemEntity,pos,SoundEvents.GLASS_HIT,SoundSource.NEUTRAL,2.0f,1.5f);

    }

}
