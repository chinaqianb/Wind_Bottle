package minecraftgtyf1.wind_bottle;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import minecraftgtyf1.wind_bottle.mixin.ChangeOminousItemSpawnerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.OminousItemSpawnerEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.slf4j.Logger;

import javax.swing.*;
import java.util.List;

public class wind_event  {
    private static Logger log = LogUtils.getLogger();
    public static boolean getEvent(World world,PlayerEntity player,Hand hand,Entity entity) {
     boolean result =false;
        ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() == Items.GLASS_BOTTLE) {
                
                if (entity.getType() == EntityType.BREEZE) {
                    if (!world.isClient()) {
                    stack.decrement(1);
                    player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());
                }else {

                    play_wind_sound(world, player);
                     }
                    result= true;
                } else if (entity.getType() == EntityType.WIND_CHARGE) {
                if (!world.isClient()){
                        stack.decrement(1);
                        entity.kill();
                        player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());
                    }else {

                    play_wind_sound(world, player);
                }
                    result= true;
                } else if (entity.getType() == EntityType.BREEZE_WIND_CHARGE) {
                    if (!world.isClient()) {
                        stack.decrement(1);
                        entity.kill();
                        player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());
                    }else {
                        play_wind_sound(world, player);
                    }
                    result= true;
                }
            }
//                if (entity instanceof OminousItemSpawnerEntity){
//
//                    if (!world.isClient()){
//                        player.sendMessage(Text.keybind("get it"));
//
//                        OminousItemSpawnerEntity ominousItemSpawner=(OminousItemSpawnerEntity) entity;
//                       ItemStack omstack= ominousItemSpawner.getItem();
//
//                       player.giveItemStack(omstack);
//                       entity.kill();
//                    }
//                    result=true;
//                }

        return result;

    }
    public static boolean get_OminousItem(PlayerEntity player,World world,ItemStack stack){
        boolean result=false;
        if (stack.getItem()==Items.FLINT) {
            List<OminousItemSpawnerEntity> omEntity = world.getEntitiesByClass(OminousItemSpawnerEntity.class, player.getBoundingBox().expand(2.0), (e) -> e != null && e.isAlive() && e instanceof OminousItemSpawnerEntity);
            if (!omEntity.isEmpty()) {
                OminousItemSpawnerEntity OmiE = omEntity.getFirst();
                if (!world.isClient()) {
                    ItemStack Omitem = OmiE.getItem();
                    if (OmiE.age>60) {
                        player.giveItemStack(Omitem);
                        ((ChangeOminousItemSpawnerEntity) OmiE).setnewItem(ItemStack.EMPTY);
                        result = true;
                    }
                }

            }
        }
        return result;
    }
static int tick=0;
    static  boolean hasFox=false;
    public static void serverTickEvent(ServerPlayerEntity players,ServerWorld world){
        Box box=new Box(players.getX()-10,players.getY()-10,players.getZ()-10,players.getX()+10,players.getY()+10,players.getZ()+10);
       List<Entity> entitys= world.getOtherEntities(players,box);
       for (Entity entity:entitys) {
           hasFox = entity.getType() == EntityType.FOX;
           if (entity instanceof ItemEntity) {
               ItemEntity itemEntity = (ItemEntity) entity;
               if (itemEntity.getStack().getItem() == Wind_bottle.wind_bottle) {
                   if (world.isDay()&&!hasFox) {
                       tick++;
//                   log.info("tickvale" + itemEntity.getItemAge());
                       if (tick == 200) {
                           itemEntityHander(world, itemEntity);
                           itemEntity.kill();

                           tick = 0;
                       }
                   }
               }
           } else if (entity.getType()==EntityType.FOX) {

               FoxEntity fox =(FoxEntity) entity;
               if (fox.getMainHandStack().getItem()==Wind_bottle.wind_bottle){
                   tick++;
                   if (tick==100){
                       world.spawnEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), Items.GLASS_BOTTLE.getDefaultStack()));
                       world.spawnEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), Items.WIND_CHARGE.getDefaultStack()));
                       fox.setStackInHand(fox.getActiveHand(),ItemStack.EMPTY);
                       tick=0;
                   }
               }
           }


       }

    }
    private static void itemEntityHander(ServerWorld world,ItemEntity itemEntity){
        for (int i=0;i<itemEntity.getStack().getCount();i++) {
            world.spawnEntity(new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), Items.GLASS_BOTTLE.getDefaultStack()));
            world.spawnEntity(new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), Items.WIND_CHARGE.getDefaultStack()));
            play_wind_sound(world, world.getClosestPlayer(itemEntity, 20f));
        }
    }




    public static void play_wind_sound(World world,PlayerEntity player){
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 2.0F, 1.5F);
    }
    public void save(int i){

        Codec<Integer> value =Codec.INT.fieldOf("wind").codec();
        DataResult<JsonElement> result =value.encodeStart(JsonOps.INSTANCE,i);
        JsonElement element =result.resultOrPartial(log::error).orElseThrow();
    }

}
