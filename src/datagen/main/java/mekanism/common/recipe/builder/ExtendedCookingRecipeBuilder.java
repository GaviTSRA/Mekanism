package mekanism.common.recipe.builder;

import com.google.gson.JsonObject;
import mekanism.api.JsonConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.DataGenJsonConstants;
import mekanism.common.util.RegistryUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

@NothingNullByDefault
public class ExtendedCookingRecipeBuilder extends BaseRecipeBuilder<ExtendedCookingRecipeBuilder> {

    private final RecipeSerializer<?> serializer;
    private final Ingredient ingredient;
    private final int cookingTime;
    private float experience;

    private ExtendedCookingRecipeBuilder(RecipeSerializer<? extends AbstractCookingRecipe> serializer, ItemLike result, int count, Ingredient ingredient, int cookingTime) {
        super(serializer, result, count);
        this.serializer = serializer;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
    }

    public static ExtendedCookingRecipeBuilder blasting(ItemLike result, Ingredient ingredient, int cookingTime) {
        return blasting(result, 1, ingredient, cookingTime);
    }

    public static ExtendedCookingRecipeBuilder blasting(ItemLike result, int count, Ingredient ingredient, int cookingTime) {
        return new ExtendedCookingRecipeBuilder(RecipeSerializer.BLASTING_RECIPE, result, count, ingredient, cookingTime);
    }

    public static ExtendedCookingRecipeBuilder campfire(ItemLike result, Ingredient ingredient, int cookingTime) {
        return campfire(result, 1, ingredient, cookingTime);
    }

    public static ExtendedCookingRecipeBuilder campfire(ItemLike result, int count, Ingredient ingredient, int cookingTime) {
        return new ExtendedCookingRecipeBuilder(RecipeSerializer.CAMPFIRE_COOKING_RECIPE, result, count, ingredient, cookingTime);
    }

    public static ExtendedCookingRecipeBuilder smelting(ItemLike result, Ingredient ingredient, int cookingTime) {
        return smelting(result, 1, ingredient, cookingTime);
    }

    public static ExtendedCookingRecipeBuilder smelting(ItemLike result, int count, Ingredient ingredient, int cookingTime) {
        return new ExtendedCookingRecipeBuilder(RecipeSerializer.SMELTING_RECIPE, result, count, ingredient, cookingTime);
    }

    public static ExtendedCookingRecipeBuilder smoking(ItemLike result, Ingredient ingredient, int cookingTime) {
        return smoking(result, 1, ingredient, cookingTime);
    }

    public static ExtendedCookingRecipeBuilder smoking(ItemLike result, int count, Ingredient ingredient, int cookingTime) {
        return new ExtendedCookingRecipeBuilder(RecipeSerializer.SMOKING_RECIPE, result, count, ingredient, cookingTime);
    }

    public ExtendedCookingRecipeBuilder experience(float experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("Experience cannot be negative.");
        }
        this.experience = experience;
        return this;
    }

    @Override
    protected RecipeResult getResult(ResourceLocation id) {
        return new Result(id);
    }

    @Override//Copy of #determineRecipeCategory
    protected StringRepresentable determineBookCategory() {
        if (serializer == RecipeSerializer.SMELTING_RECIPE) {
            if (result.isEdible()) {
                return CookingBookCategory.FOOD;
            }
            return result instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
        } else if (serializer == RecipeSerializer.BLASTING_RECIPE) {
            return result instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
        } else if (serializer != RecipeSerializer.SMOKING_RECIPE && serializer != RecipeSerializer.CAMPFIRE_COOKING_RECIPE) {
            throw new IllegalStateException("Unknown cooking recipe type");
        }
        return CookingBookCategory.FOOD;
    }

    public class Result extends BaseRecipeResult {

        public Result(ResourceLocation id) {
            super(id);
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            super.serializeRecipeData(json);
            json.add(JsonConstants.INGREDIENT, ingredient.toJson());
            json.addProperty(DataGenJsonConstants.COOKING_TIME, cookingTime);
            if (experience > 0) {
                json.addProperty(DataGenJsonConstants.EXPERIENCE, experience);
            }
        }

        @Override
        protected void serializeResult(JsonObject json) {
            if (count == 1) {
                json.addProperty(DataGenJsonConstants.RESULT, RegistryUtils.getName(result).toString());
            } else {
                super.serializeResult(json);
            }
        }
    }
}