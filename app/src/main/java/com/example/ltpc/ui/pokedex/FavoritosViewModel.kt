package com.example.ltpc.ui.pokedex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ltpc.data.FavoritosRepository
import com.example.ltpc.data.PokemonResumen
import com.example.ltpc.data.local.PokemonDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritosUiState(
    val favoritos: List<PokemonResumen> = emptyList(),
    val cargando: Boolean = true
)

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {

    private val favoritosRepositorio =
        FavoritosRepository(PokemonDatabase.obtener(application).favoritoDao())

    val uiState: StateFlow<FavoritosUiState> = favoritosRepositorio.observarFavoritos()
        .map { lista -> FavoritosUiState(favoritos = lista, cargando = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritosUiState()
        )

    fun quitar(pokemon: PokemonResumen) {
        viewModelScope.launch { favoritosRepositorio.quitar(pokemon.id) }
    }
}
