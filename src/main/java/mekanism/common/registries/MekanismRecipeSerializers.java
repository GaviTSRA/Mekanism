package mekanism.common.registries;

import mekanism.api.recipes.ChemicalCrystallizerRecipe;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.ElectrolysisRecipe;
import mekanism.api.recipes.FluidSlurryToSlurryRecipe;
import mekanism.api.recipes.FluidToFluidRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.ItemStackToEnergyRecipe;
import mekanism.api.recipes.ItemStackToGasRecipe;
import mekanism.api.recipes.ItemStackToInfuseTypeRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ItemStackToPigmentRecipe;
import mekanism.api.recipes.MetallurgicInfuserRecipe;
import mekanism.api.recipes.NucleosynthesizingRecipe;
import mekanism.api.recipes.PaintingRecipe;
import mekanism.api.recipes.PigmentMixingRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.RotaryRecipe;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.common.Mekanism;
import mekanism.common.recipe.bin.BinExtractRecipe;
import mekanism.common.recipe.bin.BinInsertRecipe;
import mekanism.common.recipe.impl.ActivatingIRecipe;
import mekanism.common.recipe.impl.CentrifugingIRecipe;
import mekanism.common.recipe.impl.ChemicalCrystallizerIRecipe;
import mekanism.common.recipe.impl.ChemicalDissolutionIRecipe;
import mekanism.common.recipe.impl.ChemicalInfuserIRecipe;
import mekanism.common.recipe.impl.ChemicalOxidizerIRecipe;
import mekanism.common.recipe.impl.CombinerIRecipe;
import mekanism.common.recipe.impl.CompressingIRecipe;
import mekanism.common.recipe.impl.CrushingIRecipe;
import mekanism.common.recipe.impl.ElectrolysisIRecipe;
import mekanism.common.recipe.impl.EnergyConversionIRecipe;
import mekanism.common.recipe.impl.EnrichingIRecipe;
import mekanism.common.recipe.impl.FluidSlurryToSlurryIRecipe;
import mekanism.common.recipe.impl.FluidToFluidIRecipe;
import mekanism.common.recipe.impl.GasConversionIRecipe;
import mekanism.common.recipe.impl.InfusionConversionIRecipe;
import mekanism.common.recipe.impl.InjectingIRecipe;
import mekanism.common.recipe.impl.MetallurgicInfuserIRecipe;
import mekanism.common.recipe.impl.NucleosynthesizingIRecipe;
import mekanism.common.recipe.impl.PaintingIRecipe;
import mekanism.common.recipe.impl.PigmentExtractingIRecipe;
import mekanism.common.recipe.impl.PigmentMixingIRecipe;
import mekanism.common.recipe.impl.PressurizedReactionIRecipe;
import mekanism.common.recipe.impl.PurifyingIRecipe;
import mekanism.common.recipe.impl.RotaryIRecipe;
import mekanism.common.recipe.impl.SawmillIRecipe;
import mekanism.common.recipe.impl.SmeltingIRecipe;
import mekanism.common.recipe.serializer.ChemicalCrystallizerRecipeSerializer;
import mekanism.common.recipe.serializer.ChemicalDissolutionRecipeSerializer;
import mekanism.common.recipe.serializer.ChemicalInfuserRecipeSerializer;
import mekanism.common.recipe.serializer.CombinerRecipeSerializer;
import mekanism.common.recipe.serializer.ElectrolysisRecipeSerializer;
import mekanism.common.recipe.serializer.FluidSlurryToSlurryRecipeSerializer;
import mekanism.common.recipe.serializer.FluidToFluidRecipeSerializer;
import mekanism.common.recipe.serializer.GasToGasRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackGasToItemStackRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackToEnergyRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackToGasRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackToInfuseTypeRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackToItemStackRecipeSerializer;
import mekanism.common.recipe.serializer.ItemStackToPigmentRecipeSerializer;
import mekanism.common.recipe.serializer.MetallurgicInfuserRecipeSerializer;
import mekanism.common.recipe.serializer.NucleosynthesizingRecipeSerializer;
import mekanism.common.recipe.serializer.PaintingRecipeSerializer;
import mekanism.common.recipe.serializer.PigmentMixingRecipeSerializer;
import mekanism.common.recipe.serializer.PressurizedReactionRecipeSerializer;
import mekanism.common.recipe.serializer.RotaryRecipeSerializer;
import mekanism.common.recipe.serializer.SawmillRecipeSerializer;
import mekanism.common.recipe.serializer.WrappedShapedRecipeSerializer;
import mekanism.common.recipe.upgrade.MekanismShapedRecipe;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class MekanismRecipeSerializers {

    private MekanismRecipeSerializers() {
    }

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Mekanism.MODID);

    public static final RecipeSerializerRegistryObject<ItemStackToItemStackRecipe> CRUSHING = RECIPE_SERIALIZERS.register("crushing", () -> new ItemStackToItemStackRecipeSerializer<>(CrushingIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackToItemStackRecipe> ENRICHING = RECIPE_SERIALIZERS.register("enriching", () -> new ItemStackToItemStackRecipeSerializer<>(EnrichingIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackToItemStackRecipe> SMELTING = RECIPE_SERIALIZERS.register("smelting", () -> new ItemStackToItemStackRecipeSerializer<>(SmeltingIRecipe::new));

    public static final RecipeSerializerRegistryObject<ChemicalInfuserRecipe> CHEMICAL_INFUSING = RECIPE_SERIALIZERS.register("chemical_infusing", () -> new ChemicalInfuserRecipeSerializer<>(ChemicalInfuserIRecipe::new));

    public static final RecipeSerializerRegistryObject<CombinerRecipe> COMBINING = RECIPE_SERIALIZERS.register("combining", () -> new CombinerRecipeSerializer<>(CombinerIRecipe::new));

    public static final RecipeSerializerRegistryObject<ElectrolysisRecipe> SEPARATING = RECIPE_SERIALIZERS.register("separating", () -> new ElectrolysisRecipeSerializer<>(ElectrolysisIRecipe::new));

    public static final RecipeSerializerRegistryObject<FluidSlurryToSlurryRecipe> WASHING = RECIPE_SERIALIZERS.register("washing", () -> new FluidSlurryToSlurryRecipeSerializer<>(FluidSlurryToSlurryIRecipe::new));

    public static final RecipeSerializerRegistryObject<FluidToFluidRecipe> EVAPORATING = RECIPE_SERIALIZERS.register("evaporating", () -> new FluidToFluidRecipeSerializer<>(FluidToFluidIRecipe::new));

    public static final RecipeSerializerRegistryObject<GasToGasRecipe> ACTIVATING = RECIPE_SERIALIZERS.register("activating", () -> new GasToGasRecipeSerializer<>(ActivatingIRecipe::new));
    public static final RecipeSerializerRegistryObject<GasToGasRecipe> CENTRIFUGING = RECIPE_SERIALIZERS.register("centrifuging", () -> new GasToGasRecipeSerializer<>(CentrifugingIRecipe::new));

    public static final RecipeSerializerRegistryObject<ChemicalCrystallizerRecipe> CRYSTALLIZING = RECIPE_SERIALIZERS.register("crystallizing", () -> new ChemicalCrystallizerRecipeSerializer<>(ChemicalCrystallizerIRecipe::new));

    public static final RecipeSerializerRegistryObject<ChemicalDissolutionRecipe> DISSOLUTION = RECIPE_SERIALIZERS.register("dissolution", () -> new ChemicalDissolutionRecipeSerializer<>(ChemicalDissolutionIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> COMPRESSING = RECIPE_SERIALIZERS.register("compressing", () -> new ItemStackGasToItemStackRecipeSerializer<>(CompressingIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> PURIFYING = RECIPE_SERIALIZERS.register("purifying", () -> new ItemStackGasToItemStackRecipeSerializer<>(PurifyingIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackGasToItemStackRecipe> INJECTING = RECIPE_SERIALIZERS.register("injecting", () -> new ItemStackGasToItemStackRecipeSerializer<>(InjectingIRecipe::new));

    public static final RecipeSerializerRegistryObject<NucleosynthesizingRecipe> NUCLEOSYNTHESIZING = RECIPE_SERIALIZERS.register("nucleosynthesizing", () -> new NucleosynthesizingRecipeSerializer<>(NucleosynthesizingIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackToEnergyRecipe> ENERGY_CONVERSION = RECIPE_SERIALIZERS.register("energy_conversion", () -> new ItemStackToEnergyRecipeSerializer<>(EnergyConversionIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackToGasRecipe> GAS_CONVERSION = RECIPE_SERIALIZERS.register("gas_conversion", () -> new ItemStackToGasRecipeSerializer<>(GasConversionIRecipe::new));
    public static final RecipeSerializerRegistryObject<ItemStackToGasRecipe> OXIDIZING = RECIPE_SERIALIZERS.register("oxidizing", () -> new ItemStackToGasRecipeSerializer<>(ChemicalOxidizerIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackToInfuseTypeRecipe> INFUSION_CONVERSION = RECIPE_SERIALIZERS.register("infusion_conversion", () -> new ItemStackToInfuseTypeRecipeSerializer<>(InfusionConversionIRecipe::new));

    public static final RecipeSerializerRegistryObject<ItemStackToPigmentRecipe> PIGMENT_EXTRACTING = RECIPE_SERIALIZERS.register("pigment_extracting", () -> new ItemStackToPigmentRecipeSerializer<>(PigmentExtractingIRecipe::new));

    public static final RecipeSerializerRegistryObject<PigmentMixingRecipe> PIGMENT_MIXING = RECIPE_SERIALIZERS.register("pigment_mixing", () -> new PigmentMixingRecipeSerializer<>(PigmentMixingIRecipe::new));

    public static final RecipeSerializerRegistryObject<MetallurgicInfuserRecipe> METALLURGIC_INFUSING = RECIPE_SERIALIZERS.register("metallurgic_infusing", () -> new MetallurgicInfuserRecipeSerializer<>(MetallurgicInfuserIRecipe::new));

    public static final RecipeSerializerRegistryObject<PaintingRecipe> PAINTING = RECIPE_SERIALIZERS.register("painting", () -> new PaintingRecipeSerializer<>(PaintingIRecipe::new));

    public static final RecipeSerializerRegistryObject<PressurizedReactionRecipe> REACTION = RECIPE_SERIALIZERS.register("reaction", () -> new PressurizedReactionRecipeSerializer<>(PressurizedReactionIRecipe::new));

    public static final RecipeSerializerRegistryObject<RotaryRecipe> ROTARY = RECIPE_SERIALIZERS.register("rotary", () -> new RotaryRecipeSerializer<>(new RotaryIRecipe.Factory()));

    public static final RecipeSerializerRegistryObject<SawmillRecipe> SAWING = RECIPE_SERIALIZERS.register("sawing", () -> new SawmillRecipeSerializer<>(SawmillIRecipe::new));

    public static final RecipeSerializerRegistryObject<MekanismShapedRecipe> MEK_DATA = RECIPE_SERIALIZERS.register("mek_data", () -> new WrappedShapedRecipeSerializer<>(MekanismShapedRecipe::new));
    public static final RecipeSerializerRegistryObject<BinInsertRecipe> BIN_INSERT = RECIPE_SERIALIZERS.register("bin_insert", () -> new SimpleCraftingRecipeSerializer<>(BinInsertRecipe::new));
    public static final RecipeSerializerRegistryObject<BinExtractRecipe> BIN_EXTRACT = RECIPE_SERIALIZERS.register("bin_extract", () -> new SimpleCraftingRecipeSerializer<>(BinExtractRecipe::new));
}