package minecraftgtyf1.wind_bottle;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.OminousItemSpawnerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

public class Wind_bottle implements ModInitializer {
    public static final Item wind_bottle =new Item(new Item.Settings().maxCount(64));
    public static final RecipeSerializer<wind_Recipe> recipe =RecipeSerializer.register("crafting_wind_bottle",new SpecialRecipeSerializer<>(wind_Recipe::new));
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

   }
}
