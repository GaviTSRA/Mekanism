package mekanism.api.recipes.cache;

import javax.annotation.ParametersAreNonnullByDefault;
import mekanism.api.annotations.NonNull;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.recipes.MetallurgicInfuserRecipe;
import mekanism.api.recipes.cache.chemical.ItemStackChemicalToItemStackCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.chemical.InfusionStackIngredient;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.item.ItemStack;

@ParametersAreNonnullByDefault
public class MetallurgicInfuserCachedRecipe extends ItemStackChemicalToItemStackCachedRecipe<InfuseType, InfusionStack, InfusionStackIngredient, MetallurgicInfuserRecipe> {

    //TODO - 1.17: Change the order of infusion input handler and item input handler
    public MetallurgicInfuserCachedRecipe(MetallurgicInfuserRecipe recipe, IInputHandler<@NonNull InfusionStack> infusionInputHandler,
          IInputHandler<@NonNull ItemStack> itemInputHandler, IOutputHandler<@NonNull ItemStack> outputHandler) {
        super(recipe, itemInputHandler, infusionInputHandler, outputHandler);
    }
}