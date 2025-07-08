# Recipe Corner - Android App

A modern Android application built with Jetpack Compose that allows users to input ingredients and discover delicious baking recipes dynamically using API integration.

## Features

- **Ingredient Input**: Add and manage ingredients with an intuitive interface
- **Recipe Generation**: Find recipes based on available ingredients
- **Baking Recipes**: Browse curated baking recipes and ideas
- **Modern UI**: Beautiful Material Design 3 interface with Jetpack Compose
- **Recipe Details**: View complete recipe information including ingredients, instructions, and cooking time
- **Responsive Design**: Optimized for various screen sizes

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Coroutines**: For asynchronous operations
- **API**: Spoonacular Recipe API

## Project Structure

```
app/src/main/java/com/example/recipecorner/
├── data/
│   └── Recipe.kt                 # Data models
├── network/
│   ├── RecipeApiService.kt       # API service interface
│   └── NetworkModule.kt          # Retrofit configuration
├── repository/
│   └── RecipeRepository.kt       # Data repository
├── ui/
│   ├── components/
│   │   ├── IngredientInput.kt    # Ingredient input component
│   │   └── RecipeCard.kt         # Recipe display component
│   ├── screens/
│   │   └── RecipeScreen.kt       # Main recipe screen
│   └── theme/                    # App theming
├── viewmodel/
│   └── RecipeViewModel.kt        # ViewModel for state management
└── MainActivity.kt               # Main activity
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Kotlin 1.9.0 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd RecipeCorner
   ```

2. **Get API Key**
   - Sign up at [Spoonacular](https://spoonacular.com/food-api)
   - Get your free API key
   - Replace `YOUR_SPOONACULAR_API_KEY` in `NetworkModule.kt` with your actual API key

3. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Run the app on an emulator or physical device

### API Configuration

The app uses the Spoonacular Recipe API. To configure:

1. Open `app/src/main/java/com/example/recipecorner/network/NetworkModule.kt`
2. Replace `YOUR_SPOONACULAR_API_KEY` with your actual API key
3. The app includes mock data as fallback when API is unavailable

## Usage

1. **Add Ingredients**: Use the ingredient input field to add ingredients you have available
2. **Find Recipes**: Click "Find Recipes" to search for recipes using your ingredients
3. **Browse Baking Ideas**: Click "Baking Ideas" to explore curated baking recipes
4. **View Recipe Details**: Tap on any recipe card to expand and view full details
5. **Manage Ingredients**: Remove individual ingredients or clear all at once

## Key Features Implementation

### Ingredient Management
- Dynamic ingredient input with validation
- Chip-based ingredient display
- Add/remove individual ingredients
- Clear all ingredients functionality

### Recipe Search
- API-based recipe search by ingredients
- Fallback to mock data when API unavailable
- Loading states and error handling
- Recipe filtering and ranking

### UI/UX Design
- Material Design 3 components
- Responsive layout
- Smooth animations and transitions
- Intuitive navigation
- Accessibility support

## Architecture

The app follows MVVM architecture pattern:

- **Model**: Data classes and repository layer
- **View**: Jetpack Compose UI components
- **ViewModel**: State management and business logic

### Data Flow
1. User interacts with UI
2. ViewModel processes user actions
3. Repository handles data operations
4. API service makes network requests
5. Data flows back through the chain to update UI

## Dependencies

Key dependencies used in the project:

```kotlin
// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")

// Image Loading
implementation("io.coil-kt:coil-compose:2.5.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
```

## Future Enhancements

- Recipe favorites and bookmarking
- Recipe sharing functionality
- Nutritional information display
- Recipe categories and filtering
- Offline recipe storage
- User preferences and dietary restrictions
- Recipe rating and reviews
- Shopping list generation

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [Spoonacular API](https://spoonacular.com/food-api) for recipe data
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI development
- [Material Design 3](https://m3.material.io/) for design guidelines 