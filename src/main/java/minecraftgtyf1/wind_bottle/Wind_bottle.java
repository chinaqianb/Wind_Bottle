package minecraftgtyf1.wind_bottle;



import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Wind_bottle implements ModInitializer {
    public static final Item wind_bottle =new Item(new Item.Settings().maxCount(64));
//    public static final RecipeSerializer<wind_Recipe> recipe =RecipeSerializer.register("crafting_wind_bottle",new SpecialRecipeSerializer<>(wind_Recipe::new));
    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of("wind_bottle","wind_bottle"),wind_bottle);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(wind_bottle));

        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
          boolean res=  wind_event.getEvent(world,player,hand,entity);
          if (res){
              return ActionResult.SUCCESS;
          }else return ActionResult.PASS;

        }));
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack stack =player.getStackInHand(hand);
            boolean result =wind_event.get_OminousItem(player,world,stack);
            return result ? TypedActionResult.success(stack) : TypedActionResult.pass(stack);
        }));
        ServerTickEvents.END_WORLD_TICK.register((serverWorld -> {
           List<ServerPlayerEntity> players= serverWorld.getPlayers();
            for (ServerPlayerEntity player:players){
                wind_event.serverTickEvent(player,serverWorld);
            }

        }));

   }

}
