package com.example.recipecorner.network

import com.example.recipecorner.data.Recipe
import com.example.recipecorner.data.RecipeByIngredients
import com.example.recipecorner.data.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    
    @GET("recipes/findByIngredients")
    suspend fun findRecipesByIngredients(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("ranking") ranking: Int = 2,
        @Query("ignorePantry") ignorePantry: Boolean = true,
        @Query("apiKey") apiKey: String
    ): List<RecipeByIngredients>
    
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String? = null,
        @Query("cuisine") cuisine: String? = null,
        @Query("diet") diet: String? = null,
        @Query("intolerances") intolerances: String? = null,
        @Query("maxReadyTime") maxReadyTime: Int? = null,
        @Query("number") number: Int = 10,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("fillIngredients") fillIngredients: Boolean = true,
        @Query("apiKey") apiKey: String
    ): RecipeSearchResponse
    
    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @retrofit2.http.Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): Recipe
} 