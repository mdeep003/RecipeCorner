package com.example.recipecorner.data

// Recipe from findByIngredients endpoint (simplified)
data class RecipeByIngredients(
    val id: Int,
    val title: String?,
    val image: String?,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int,
    val missedIngredients: List<Ingredient>?,
    val usedIngredients: List<Ingredient>?,
    val unusedIngredients: List<Ingredient>?
)

// Full recipe from getRecipeInformation endpoint
data class Recipe(
    val id: Int,
    val title: String?,
    val image: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val instructions: String?,
    val extendedIngredients: List<RecipeIngredient>?, // This is the correct field name
    val ingredients: List<Ingredient>? = null, // For backward compatibility
    val nutrition: Nutrition? = null
)

// Ingredient from findByIngredients endpoint
data class Ingredient(
    val id: Int,
    val name: String?,
    val amount: Double,
    val unit: String,
    val original: String?
)

// Ingredient from getRecipeInformation endpoint (different structure)
data class RecipeIngredient(
    val id: Int,
    val name: String?,
    val localizedName: String?,
    val image: String?,
    val amount: Double,
    val unit: String,
    val original: String?,
    val originalName: String?,
    val meta: List<String>?,
    val aisle: String?
)

data class Nutrition(
    val calories: Double,
    val protein: String,
    val fat: String,
    val carbohydrates: String
)

data class RecipeSearchResponse(
    val results: List<Recipe>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

data class RecipeRequest(
    val ingredients: List<String>,
    val cuisine: String = "baking",
    val diet: String? = null,
    val intolerances: String? = null,
    val maxReadyTime: Int = 60
) 