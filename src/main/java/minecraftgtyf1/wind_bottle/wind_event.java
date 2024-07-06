package minecraftgtyf1.wind_bottle;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.impl.event.interaction.InteractionEventsRouter;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.recipe.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.EntityList;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class wind_event  {

    public static void getEvent(World world,PlayerEntity player,Hand hand,Entity entity){

            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem()==Items.GLASS_BOTTLE){
                if (world.isClient()){
                    world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 2.0F, 1.5F);
                if (entity.getType() == EntityType.BREEZE) {
                    stack.decrement(1);
                    player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());
                }
                if (entity.getType()==EntityType.WIND_CHARGE){
                    stack.decrement(1);
                    player.giveItemStack(Wind_bottle.wind_bottle.getDefaultStack());

                }
            }

        }
    }
}
