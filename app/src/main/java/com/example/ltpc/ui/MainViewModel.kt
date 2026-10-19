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

class MainViewModel : ViewModel() {

    private val timeHelper = TimeHelper()
    private val beepHelper = BeepHelper()

    private val mealApi = MealApiService.create()
    private val cocktailApi = CocktailApiService.create()
    private val pokeApi = PokeApiService.create()

    private val _recipeState = MutableLiveData<RecipeUiModel?>()
    val recipeState: LiveData<RecipeUiModel?> get() = _recipeState

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

    fun toggleTimeOffset() {
        timeHelper.toggleOffset()
        updateMode()
        playBeeps()
    }

    fun fetchMealWithPokemon() {
        viewModelScope.launch {
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
            }
        }
    }

    fun fetchCocktailWithPokemon() {
        viewModelScope.launch {
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
            }
        }
    }
}
