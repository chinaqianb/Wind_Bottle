package minecraftgtyf1.wind_bottle;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class Wind_bottle implements ModInitializer {
    public static final Item wind_bottle =new Item(new Item.Settings().maxCount(64));
    public static final RecipeSerializer<wind_Recipe> recipe =RecipeSerializer.register("crafting_wind_bottle",new SpecialRecipeSerializer<>(wind_Recipe::new));
    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of("wind_bottle","wind_bottle"),wind_bottle);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(wind_bottle));
        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
            wind_event.getEvent(world,player,hand,entity);
            return ActionResult.PASS;
        }));
    }
}
