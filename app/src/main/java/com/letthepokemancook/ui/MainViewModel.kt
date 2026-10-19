package com.letthepokemancook.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letthepokemancook.data.RecipeUiModel

class MainViewModel : ViewModel() {

    private val _recipeState = MutableLiveData<RecipeUiModel?>()
    val recipeState: LiveData<RecipeUiModel?> get() = _recipeState

    private val _isNightMode = MutableLiveData<Boolean>()
    val isNightMode: LiveData<Boolean> get() = _isNightMode

    fun updateMode() {
        TODO("Not yet implemented")
    }

    fun toggleTimeOffset() {
        TODO("Not yet implemented")
    }

    fun fetchMealWithPokemon() {
        TODO("Not yet implemented")
    }

    fun fetchCocktailWithPokemon() {
        TODO("Not yet implemented")
    }
}
