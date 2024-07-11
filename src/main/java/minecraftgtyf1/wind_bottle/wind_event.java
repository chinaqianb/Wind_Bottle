package minecraftgtyf1.wind_bottle;

import com.google.gson.JsonElement;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.Tag;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.slf4j.Logger;

public class wind_event  {
    Logger log = LogUtils.getLogger();
    public static void getEvent(World world,PlayerEntity player,Hand hand,Entity entity){

            ItemStack stack = player.getStackInHand(hand);
            if (world.isClient()){
            if (stack.getItem()==Items.GLASS_BOTTLE) {
                if (entity.getType() == EntityType.BREEZE) {
                    stack.decrement(1);
                    player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());
                    play_wind_sound(world,player);
                } else if (entity.getType() == EntityType.WIND_CHARGE) {
                    stack.decrement(1);
                    entity.kill();
                    player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());
                    play_wind_sound(world,player);
                } else if (entity.getType() == EntityType.BREEZE_WIND_CHARGE) {
                    stack.decrement(1);
                    entity.kill();
                    player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());
                    play_wind_sound(world, player);

                }
            }

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
