package com.example.ltpc.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ltpc.data.CocktailApiService
import com.example.ltpc.data.MealApiService
import com.example.ltpc.data.PokeApiService
import com.example.ltpc.data.RecipeUiModel
import com.example.ltpc.utils.BeepHelper
import com.example.ltpc.utils.TimeHelper
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppScreen {
    HOME,
    RECIPE,
    FIESTA,
    POKEDEX
}

class MainViewModel : ViewModel() {

    private val timeHelper = TimeHelper()
    private val beepHelper = BeepHelper()

    private val mealApi by lazy { MealApiService.create() }
    private val cocktailApi by lazy { CocktailApiService.create() }
    private val pokeApi by lazy { PokeApiService.create() }

    private val _currentScreen = MutableLiveData(AppScreen.HOME)
    val currentScreen: LiveData<AppScreen> get() = _currentScreen

    private val _recipeState = MutableLiveData<RecipeUiModel?>()
    val recipeState: LiveData<RecipeUiModel?> get() = _recipeState

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isNightMode = MutableLiveData<Boolean>()
    val isNightMode: LiveData<Boolean> get() = _isNightMode

    init {
        updateMode()
        playBeeps()
    }

    fun playBeeps() {
        viewModelScope.launch {
            try {
                beepHelper.playBeepSequence()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMode() {
        _isNightMode.value = timeHelper.isNightTime()
    }

    fun getFormattedCurrentTime(): String {
        return timeHelper.getFormattedCurrentTime()
    }

    fun toggleTimeOffset() {
        timeHelper.toggleOffset()
        updateMode()
        playBeeps()
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _currentScreen.value = screen
            playBeeps()
        }
    }

    fun fetchMealWithPokemon() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val mealResponse = mealApi.getRandomMeal()
                val meal = mealResponse.meals?.firstOrNull()

                val randomPokemonId = Random.nextInt(1, 1026)
                val pokemon = pokeApi.getPokemon(randomPokemonId)

                if (meal != null) {
                    _recipeState.value = RecipeUiModel(
                        title = meal.strMeal ?: "Plato Misterioso",
                        instructions = meal.strInstructions ?: "Sin instrucciones.",
                        recipeImageUrl = meal.strMealThumb ?: "",
                        pokemonName = pokemon.name.replaceFirstChar { it.uppercase() },
                        pokemonImageUrl = pokemon.sprites.front_default ?: ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchCocktailWithPokemon() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cocktailResponse = cocktailApi.getRandomCocktail()
                val cocktail = cocktailResponse.meals?.firstOrNull()

                val randomPokemonId = Random.nextInt(1, 1026)
                val pokemon = pokeApi.getPokemon(randomPokemonId)

                if (cocktail != null) {
                    _recipeState.value = RecipeUiModel(
                        title = cocktail.strMeal ?: "Bebida Misteriosa",
                        instructions = cocktail.strInstructions ?: "Sin instrucciones.",
                        recipeImageUrl = cocktail.strMealThumb ?: "",
                        pokemonName = pokemon.name.replaceFirstChar { it.uppercase() },
                        pokemonImageUrl = pokemon.sprites.front_default ?: ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
