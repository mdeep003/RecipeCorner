package com.example.recipecorner.repository

import com.example.recipecorner.data.Recipe
import com.example.recipecorner.data.RecipeSearchResponse
import com.example.recipecorner.data.Ingredient
import com.example.recipecorner.data.RecipeIngredient
import com.example.recipecorner.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository {
    
    private val apiService = NetworkModule.recipeApiService
    private val apiKey = NetworkModule.getApiKey()
    
    suspend fun findRecipesByIngredients(ingredients: List<String>): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                println("Repository: Fetching recipes from API for ingredients: $ingredients")
                
                // Convert ingredients list to comma-separated string
                val ingredientsString = ingredients.joinToString(",")
                
                // Call the API
                val recipesByIngredients = apiService.findRecipesByIngredients(
                    ingredients = ingredientsString,
                    number = 10,
                    ranking = 2,
                    ignorePantry = true,
                    apiKey = apiKey
                )
                
                println("Repository: Received ${recipesByIngredients.size} recipes from API")
                
                // Convert RecipeByIngredients to full Recipe objects
                val fullRecipes = mutableListOf<Recipe>()
                
                for (recipeByIngredients in recipesByIngredients) {
                    try {
                        // Get full recipe information
                        val fullRecipe = apiService.getRecipeInformation(
                            recipeId = recipeByIngredients.id,
                            apiKey = apiKey
                        )
                        fullRecipes.add(fullRecipe)
                        println("Repository: Added full recipe: ${fullRecipe.title}")
                    } catch (e: Exception) {
                        println("Repository: Error getting full recipe ${recipeByIngredients.id}: ${e.message}")
                        // Add a simplified recipe if we can't get full details
                        fullRecipes.add(
                            Recipe(
                                id = recipeByIngredients.id,
                                title = recipeByIngredients.title,
                                image = recipeByIngredients.image,
                                readyInMinutes = 30, // Default
                                servings = 4, // Default
                                instructions = "Instructions not available",
                                ingredients = recipeByIngredients.usedIngredients,
                                extendedIngredients = recipeByIngredients.usedIngredients?.map { ingredient ->
                                    RecipeIngredient(
                                        id = ingredient.id,
                                        name = ingredient.name,
                                        localizedName = ingredient.name,
                                        image = null,
                                        amount = ingredient.amount,
                                        unit = ingredient.unit,
                                        original = ingredient.original,
                                        originalName = ingredient.name,
                                        meta = null,
                                        aisle = null
                                    )
                                }
                            )
                        )
                    }
                }
                
                fullRecipes
            } catch (e: Exception) {
                println("Repository: Error fetching recipes from API: ${e.message}")
                // Fallback to mock data if API fails
                getMockRecipesByIngredients(ingredients)
            }
        }
    }
    
    suspend fun searchRecipes(
        query: String? = null,
        cuisine: String = "baking",
        maxReadyTime: Int = 60
    ): RecipeSearchResponse {
        return withContext(Dispatchers.IO) {
            // Always return mock data for demo purposes
            println("Repository: Returning mock baking recipes")
            RecipeSearchResponse(
                results = getMockRecipes(),
                offset = 0,
                number = 10,
                totalResults = 10
            )
        }
    }
    
    private fun convertToRecipeIngredients(ingredients: List<Ingredient>): List<RecipeIngredient> {
        return ingredients.map { ingredient ->
            RecipeIngredient(
                id = ingredient.id,
                name = ingredient.name,
                localizedName = ingredient.name,
                image = null,
                amount = ingredient.amount,
                unit = ingredient.unit,
                original = ingredient.original,
                originalName = ingredient.name,
                meta = null,
                aisle = null
            )
        }
    }
    
    private fun getMockRecipesByIngredients(ingredients: List<String>): List<Recipe> {
        val allRecipes = listOf(
            Recipe(
                id = 1,
                title = "Chocolate Chip Cookies",
                image = null,
                readyInMinutes = 25,
                servings = 24,
                instructions = "1. Preheat oven to 375°F (190°C)\n2. Cream butter and sugars until fluffy\n3. Beat in eggs and vanilla\n4. Mix in flour, baking soda, and salt\n5. Stir in chocolate chips\n6. Drop rounded tablespoons onto baking sheet\n7. Bake for 9-11 minutes until golden brown",
                ingredients = listOf(
                    Ingredient(1, "all-purpose flour", 2.5, "cups", "2 1/2 cups all-purpose flour"),
                    Ingredient(2, "butter", 1.0, "cup", "1 cup butter, softened"),
                    Ingredient(3, "brown sugar", 0.75, "cup", "3/4 cup brown sugar"),
                    Ingredient(4, "white sugar", 0.75, "cup", "3/4 cup white sugar"),
                    Ingredient(5, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(6, "vanilla extract", 2.0, "tsp", "2 tsp vanilla extract"),
                    Ingredient(7, "chocolate chips", 2.0, "cups", "2 cups chocolate chips")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(1, "all-purpose flour", 2.5, "cups", "2 1/2 cups all-purpose flour"),
                    Ingredient(2, "butter", 1.0, "cup", "1 cup butter, softened"),
                    Ingredient(3, "brown sugar", 0.75, "cup", "3/4 cup brown sugar"),
                    Ingredient(4, "white sugar", 0.75, "cup", "3/4 cup white sugar"),
                    Ingredient(5, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(6, "vanilla extract", 2.0, "tsp", "2 tsp vanilla extract"),
                    Ingredient(7, "chocolate chips", 2.0, "cups", "2 cups chocolate chips")
                ))
            ),
            Recipe(
                id = 2,
                title = "Vanilla Cupcakes",
                image = null,
                readyInMinutes = 30,
                servings = 12,
                instructions = "1. Preheat oven to 350°F (175°C)\n2. Line muffin tin with paper liners\n3. Cream butter and sugar until light and fluffy\n4. Add eggs one at a time, beating well\n5. Mix in vanilla extract\n6. Alternately add flour and milk\n7. Fill muffin cups 2/3 full\n8. Bake for 18-20 minutes",
                ingredients = listOf(
                    Ingredient(8, "all-purpose flour", 1.5, "cups", "1 1/2 cups all-purpose flour"),
                    Ingredient(9, "butter", 0.5, "cup", "1/2 cup butter, softened"),
                    Ingredient(10, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(11, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(12, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(13, "milk", 0.5, "cup", "1/2 cup milk")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(8, "all-purpose flour", 1.5, "cups", "1 1/2 cups all-purpose flour"),
                    Ingredient(9, "butter", 0.5, "cup", "1/2 cup butter, softened"),
                    Ingredient(10, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(11, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(12, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(13, "milk", 0.5, "cup", "1/2 cup milk")
                ))
            ),
            Recipe(
                id = 3,
                title = "Banana Bread",
                image = null,
                readyInMinutes = 60,
                servings = 8,
                instructions = "1. Preheat oven to 350°F (175°C)\n2. Grease a 9x5 inch loaf pan\n3. Mash bananas in a large bowl\n4. Mix in melted butter, sugar, eggs, and vanilla\n5. Stir in flour, baking soda, and salt\n6. Pour into prepared loaf pan\n7. Bake for 50-60 minutes until toothpick comes out clean",
                ingredients = listOf(
                    Ingredient(14, "ripe bananas", 3.0, "medium", "3 medium ripe bananas"),
                    Ingredient(15, "all-purpose flour", 1.5, "cups", "1 1/2 cups all-purpose flour"),
                    Ingredient(16, "butter", 0.33, "cup", "1/3 cup butter, melted"),
                    Ingredient(17, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(18, "eggs", 1.0, "large", "1 large egg"),
                    Ingredient(19, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(20, "baking soda", 1.0, "tsp", "1 tsp baking soda")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(14, "ripe bananas", 3.0, "medium", "3 medium ripe bananas"),
                    Ingredient(15, "all-purpose flour", 1.5, "cups", "1 1/2 cups all-purpose flour"),
                    Ingredient(16, "butter", 0.33, "cup", "1/3 cup butter, melted"),
                    Ingredient(17, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(18, "eggs", 1.0, "large", "1 large egg"),
                    Ingredient(19, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(20, "baking soda", 1.0, "tsp", "1 tsp baking soda")
                ))
            ),
            Recipe(
                id = 4,
                title = "Apple Pie",
                image = null,
                readyInMinutes = 90,
                servings = 8,
                instructions = "1. Preheat oven to 425°F (220°C)\n2. Prepare pie crust and place in pie dish\n3. Mix sliced apples with sugar, cinnamon, and nutmeg\n4. Fill pie crust with apple mixture\n5. Add top crust and crimp edges\n6. Cut slits in top crust\n7. Bake for 45 minutes until golden brown",
                ingredients = listOf(
                    Ingredient(21, "apples", 6.0, "medium", "6 medium apples, sliced"),
                    Ingredient(22, "pie crust", 2.0, "sheets", "2 sheets pie crust"),
                    Ingredient(23, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(24, "cinnamon", 1.0, "tsp", "1 tsp cinnamon"),
                    Ingredient(25, "nutmeg", 0.25, "tsp", "1/4 tsp nutmeg"),
                    Ingredient(26, "butter", 2.0, "tbsp", "2 tbsp butter")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(21, "apples", 6.0, "medium", "6 medium apples, sliced"),
                    Ingredient(22, "pie crust", 2.0, "sheets", "2 sheets pie crust"),
                    Ingredient(23, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(24, "cinnamon", 1.0, "tsp", "1 tsp cinnamon"),
                    Ingredient(25, "nutmeg", 0.25, "tsp", "1/4 tsp nutmeg"),
                    Ingredient(26, "butter", 2.0, "tbsp", "2 tbsp butter")
                ))
            ),
            Recipe(
                id = 5,
                title = "Chocolate Cake",
                image = null,
                readyInMinutes = 45,
                servings = 12,
                instructions = "1. Preheat oven to 350°F (175°C)\n2. Grease and flour two 9-inch round cake pans\n3. Mix flour, cocoa, baking powder, and salt\n4. Cream butter and sugar until fluffy\n5. Add eggs and vanilla\n6. Alternately add flour mixture and milk\n7. Bake for 25-30 minutes",
                ingredients = listOf(
                    Ingredient(27, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(28, "cocoa powder", 0.75, "cup", "3/4 cup cocoa powder"),
                    Ingredient(29, "sugar", 1.5, "cups", "1 1/2 cups sugar"),
                    Ingredient(30, "butter", 0.5, "cup", "1/2 cup butter"),
                    Ingredient(31, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(32, "milk", 1.0, "cup", "1 cup milk"),
                    Ingredient(33, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(27, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(28, "cocoa powder", 0.75, "cup", "3/4 cup cocoa powder"),
                    Ingredient(29, "sugar", 1.5, "cups", "1 1/2 cups sugar"),
                    Ingredient(30, "butter", 0.5, "cup", "1/2 cup butter"),
                    Ingredient(31, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(32, "milk", 1.0, "cup", "1 cup milk"),
                    Ingredient(33, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract")
                ))
            ),
            Recipe(
                id = 6,
                title = "Blueberry Muffins",
                image = null,
                readyInMinutes = 35,
                servings = 12,
                instructions = "1. Preheat oven to 375°F (190°C)\n2. Line muffin tin with paper liners\n3. Mix flour, sugar, baking powder, and salt\n4. In another bowl, mix milk, oil, and egg\n5. Combine wet and dry ingredients\n6. Fold in blueberries\n7. Fill muffin cups and bake for 20-25 minutes",
                ingredients = listOf(
                    Ingredient(34, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(35, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(36, "baking powder", 2.0, "tsp", "2 tsp baking powder"),
                    Ingredient(37, "milk", 1.0, "cup", "1 cup milk"),
                    Ingredient(38, "vegetable oil", 0.33, "cup", "1/3 cup vegetable oil"),
                    Ingredient(39, "egg", 1.0, "large", "1 large egg"),
                    Ingredient(40, "blueberries", 1.0, "cup", "1 cup fresh blueberries")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(34, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(35, "sugar", 0.75, "cup", "3/4 cup sugar"),
                    Ingredient(36, "baking powder", 2.0, "tsp", "2 tsp baking powder"),
                    Ingredient(37, "milk", 1.0, "cup", "1 cup milk"),
                    Ingredient(38, "vegetable oil", 0.33, "cup", "1/3 cup vegetable oil"),
                    Ingredient(39, "egg", 1.0, "large", "1 large egg"),
                    Ingredient(40, "blueberries", 1.0, "cup", "1 cup fresh blueberries")
                ))
            ),
            Recipe(
                id = 7,
                title = "Strawberry Cheesecake",
                image = null,
                readyInMinutes = 120,
                servings = 12,
                instructions = "1. Preheat oven to 350°F (175°C)\n2. Mix graham cracker crumbs, butter, and sugar for crust\n3. Press into springform pan and bake for 10 minutes\n4. Beat cream cheese, sugar, and vanilla until smooth\n5. Add eggs one at a time\n6. Pour over crust and bake for 50-55 minutes\n7. Top with fresh strawberries",
                ingredients = listOf(
                    Ingredient(41, "graham crackers", 1.5, "cups", "1 1/2 cups graham cracker crumbs"),
                    Ingredient(42, "butter", 0.33, "cup", "1/3 cup butter, melted"),
                    Ingredient(43, "cream cheese", 24.0, "oz", "24 oz cream cheese, softened"),
                    Ingredient(44, "sugar", 1.0, "cup", "1 cup sugar"),
                    Ingredient(45, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(46, "eggs", 3.0, "large", "3 large eggs"),
                    Ingredient(47, "strawberries", 2.0, "cups", "2 cups fresh strawberries")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(41, "graham crackers", 1.5, "cups", "1 1/2 cups graham cracker crumbs"),
                    Ingredient(42, "butter", 0.33, "cup", "1/3 cup butter, melted"),
                    Ingredient(43, "cream cheese", 24.0, "oz", "24 oz cream cheese, softened"),
                    Ingredient(44, "sugar", 1.0, "cup", "1 cup sugar"),
                    Ingredient(45, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(46, "eggs", 3.0, "large", "3 large eggs"),
                    Ingredient(47, "strawberries", 2.0, "cups", "2 cups fresh strawberries")
                ))
            ),
            Recipe(
                id = 8,
                title = "Lemon Bars",
                image = null,
                readyInMinutes = 60,
                servings = 16,
                instructions = "1. Preheat oven to 350°F (175°C)\n2. Mix flour, butter, and powdered sugar for crust\n3. Press into 9x13 pan and bake for 20 minutes\n4. Beat eggs, sugar, lemon juice, and flour for filling\n5. Pour over hot crust and bake for 25 minutes\n6. Cool and dust with powdered sugar",
                ingredients = listOf(
                    Ingredient(48, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(49, "butter", 1.0, "cup", "1 cup butter, softened"),
                    Ingredient(50, "powdered sugar", 0.5, "cup", "1/2 cup powdered sugar"),
                    Ingredient(51, "eggs", 4.0, "large", "4 large eggs"),
                    Ingredient(52, "sugar", 1.5, "cups", "1 1/2 cups sugar"),
                    Ingredient(53, "lemon juice", 0.25, "cup", "1/4 cup fresh lemon juice"),
                    Ingredient(54, "lemon zest", 1.0, "tbsp", "1 tbsp lemon zest")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(48, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(49, "butter", 1.0, "cup", "1 cup butter, softened"),
                    Ingredient(50, "powdered sugar", 0.5, "cup", "1/2 cup powdered sugar"),
                    Ingredient(51, "eggs", 4.0, "large", "4 large eggs"),
                    Ingredient(52, "sugar", 1.5, "cups", "1 1/2 cups sugar"),
                    Ingredient(53, "lemon juice", 0.25, "cup", "1/4 cup fresh lemon juice"),
                    Ingredient(54, "lemon zest", 1.0, "tbsp", "1 tbsp lemon zest")
                ))
            ),
            Recipe(
                id = 9,
                title = "Carrot Cake",
                image = null,
                readyInMinutes = 75,
                servings = 12,
                instructions = "1. Preheat oven to 350°F (175°C)\n2. Grease and flour two 9-inch round pans\n3. Mix flour, baking soda, cinnamon, and salt\n4. Beat eggs, oil, sugar, and vanilla\n5. Add flour mixture and fold in carrots\n6. Bake for 30-35 minutes\n7. Frost with cream cheese frosting",
                ingredients = listOf(
                    Ingredient(55, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(56, "baking soda", 2.0, "tsp", "2 tsp baking soda"),
                    Ingredient(57, "cinnamon", 2.0, "tsp", "2 tsp cinnamon"),
                    Ingredient(58, "eggs", 4.0, "large", "4 large eggs"),
                    Ingredient(59, "vegetable oil", 1.25, "cups", "1 1/4 cups vegetable oil"),
                    Ingredient(60, "sugar", 2.0, "cups", "2 cups sugar"),
                    Ingredient(61, "carrots", 3.0, "cups", "3 cups grated carrots"),
                    Ingredient(62, "vanilla extract", 2.0, "tsp", "2 tsp vanilla extract")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(55, "all-purpose flour", 2.0, "cups", "2 cups all-purpose flour"),
                    Ingredient(56, "baking soda", 2.0, "tsp", "2 tsp baking soda"),
                    Ingredient(57, "cinnamon", 2.0, "tsp", "2 tsp cinnamon"),
                    Ingredient(58, "eggs", 4.0, "large", "4 large eggs"),
                    Ingredient(59, "vegetable oil", 1.25, "cups", "1 1/4 cups vegetable oil"),
                    Ingredient(60, "sugar", 2.0, "cups", "2 cups sugar"),
                    Ingredient(61, "carrots", 3.0, "cups", "3 cups grated carrots"),
                    Ingredient(62, "vanilla extract", 2.0, "tsp", "2 tsp vanilla extract")
                ))
            ),
            Recipe(
                id = 10,
                title = "Peanut Butter Cookies",
                image = null,
                readyInMinutes = 25,
                servings = 24,
                instructions = "1. Preheat oven to 375°F (190°C)\n2. Cream peanut butter, butter, and sugars\n3. Beat in eggs and vanilla\n4. Mix in flour, baking soda, and salt\n5. Roll into balls and press with fork\n6. Bake for 10-12 minutes until golden",
                ingredients = listOf(
                    Ingredient(63, "peanut butter", 1.0, "cup", "1 cup creamy peanut butter"),
                    Ingredient(64, "butter", 0.5, "cup", "1/2 cup butter, softened"),
                    Ingredient(65, "brown sugar", 0.75, "cup", "3/4 cup brown sugar"),
                    Ingredient(66, "white sugar", 0.75, "cup", "3/4 cup white sugar"),
                    Ingredient(67, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(68, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(69, "all-purpose flour", 1.5, "cups", "1 1/2 cups all-purpose flour"),
                    Ingredient(70, "baking soda", 1.0, "tsp", "1 tsp baking soda")
                ),
                extendedIngredients = convertToRecipeIngredients(listOf(
                    Ingredient(63, "peanut butter", 1.0, "cup", "1 cup creamy peanut butter"),
                    Ingredient(64, "butter", 0.5, "cup", "1/2 cup butter, softened"),
                    Ingredient(65, "brown sugar", 0.75, "cup", "3/4 cup brown sugar"),
                    Ingredient(66, "white sugar", 0.75, "cup", "3/4 cup white sugar"),
                    Ingredient(67, "eggs", 2.0, "large", "2 large eggs"),
                    Ingredient(68, "vanilla extract", 1.0, "tsp", "1 tsp vanilla extract"),
                    Ingredient(69, "all-purpose flour", 1.5, "cups", "1 1/2 cups all-purpose flour"),
                    Ingredient(70, "baking soda", 1.0, "tsp", "1 tsp baking soda")
                ))
            )
        )
        
        // Filter recipes based on ingredients
        val matchingRecipes = allRecipes.filter { recipe ->
            val recipeIngredients = recipe.ingredients?.map { it.name?.lowercase() } ?: emptyList()
            val userIngredients = ingredients.map { it.lowercase() }
            
            // Check if any user ingredient matches any recipe ingredient
            userIngredients.any { userIngredient ->
                recipeIngredients.any { recipeIngredient ->
                    recipeIngredient?.contains(userIngredient) == true || 
                    userIngredient.contains(recipeIngredient ?: "")
                }
            }
        }
        
        // Debug: Print what we're looking for and what we found
        println("Repository: Looking for ingredients: $ingredients")
        println("Repository: Found ${matchingRecipes.size} matching recipes")
        matchingRecipes.forEach { recipe ->
            println("Repository: Matching recipe: ${recipe.title}")
        }
        
        // Return matching recipes, or empty list if none found
        return matchingRecipes.take(3)
    }
    
    private fun getMockRecipes(): List<Recipe> {
        return getMockRecipesByIngredients(emptyList())
    }
} 