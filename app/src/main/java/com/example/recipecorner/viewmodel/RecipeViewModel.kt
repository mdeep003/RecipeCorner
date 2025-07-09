package com.example.recipecorner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecorner.data.Recipe
import com.example.recipecorner.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    
    private val repository = RecipeRepository()
    
    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()
    
    private val _ingredients = MutableStateFlow<List<String>>(emptyList())
    val ingredients: StateFlow<List<String>> = _ingredients.asStateFlow()
    
    fun addIngredient(ingredient: String) {
        if (ingredient.isNotBlank() && !_ingredients.value.contains(ingredient.trim())) {
            _ingredients.value = _ingredients.value + ingredient.trim()
        }
    }
    
    fun removeIngredient(ingredient: String) {
        _ingredients.value = _ingredients.value.filter { it != ingredient }
        // Clear recipes if no ingredients left
        if (_ingredients.value.isEmpty()) {
            _uiState.value = _uiState.value.copy(recipes = emptyList())
        }
    }
    
    fun clearIngredients() {
        _ingredients.value = emptyList()
        _uiState.value = _uiState.value.copy(recipes = emptyList())
    }
    
    fun searchRecipes() {
        if (_ingredients.value.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Please add at least one ingredient"
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )
        
        viewModelScope.launch {
            try {
                val recipes = repository.findRecipesByIngredients(_ingredients.value)
                // --- SCORING AND SORTING LOGIC ---
                val userIngredients = _ingredients.value.map { it.lowercase() }
                val sortedRecipes = recipes.sortedWith(compareByDescending<Recipe> { recipe ->
                    // Count matched ingredients
                    val ingredients = recipe.extendedIngredients ?: emptyList()
                    ingredients.count { ing ->
                        ing.name?.lowercase() in userIngredients
                    }
                }.thenBy { it.readyInMinutes })
                // --- END SCORING AND SORTING LOGIC ---
                println("ViewModel: Received ${sortedRecipes.size} recipes (sorted)")
                sortedRecipes.forEach { recipe ->
                    println("ViewModel: Recipe ${recipe.id} - ${recipe.title}")
                    println("ViewModel: Ingredients: ${recipe.extendedIngredients?.size ?: 0}")
                    println("ViewModel: Instructions: ${recipe.instructions?.length ?: 0} chars")
                }
                _uiState.value = _uiState.value.copy(
                    recipes = sortedRecipes,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                println("ViewModel: Error loading recipes: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load recipes: ${e.message}"
                )
            }
        }
    }
    

    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class RecipeUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 