package minecraftgtyf1.wind_bottle;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

public class wind_Recipe extends SpecialCraftingRecipe {
    public wind_Recipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        List<ItemStack> take =input.getStacks();
        List<ItemStack> get =new ArrayList<>();
        for (ItemStack stack:take){
            if (!stack.isEmpty()&&stack.getItem()==Wind_bottle.wind_bottle){
               get.add(stack);
            }
        }
        if (get.size()==1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        List<ItemStack> take =input.getStacks();
        List<ItemStack> get =new ArrayList<>();
        for (ItemStack stack:take){
            if (!stack.isEmpty()&&stack.getItem()==Wind_bottle.wind_bottle){
                get.add(stack);
            }
        }
        if (get.size()==1){
            return Items.WIND_CHARGE.getDefaultStack();
        }else {
            return ItemStack.EMPTY;
        }

    }

    @Override
    public boolean fits(int width, int height) {
        return width*height>=1;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
        DefaultedList<ItemStack> list =DefaultedList.ofSize(input.getSize(),ItemStack.EMPTY);
        for (int i=0;i<list.size();++i){
            ItemStack stack=input.getStackInSlot(i);
            if (!stack.isEmpty()&&stack.getItem()==Wind_bottle.wind_bottle){
                list.set(i,Items.GLASS_BOTTLE.getDefaultStack());
            }
        }
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Wind_bottle.recipe;
    }
}
