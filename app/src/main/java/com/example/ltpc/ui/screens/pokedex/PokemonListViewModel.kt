package com.example.ltpc.ui.screens.pokedex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ltpc.data.repository.FavoritosRepository
import com.example.ltpc.data.repository.PokemonRepository
import com.example.ltpc.domain.PokemonResumen
import com.example.ltpc.data.local.PokemonDatabase
import com.example.ltpc.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonListUiState(
    val pokemones: List<PokemonResumen> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null,
    val hayMas: Boolean = true,
    val favoritosIds: Set<Int> = emptySet()
)

class PokemonListViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = PokemonRepository()
    private val favoritosRepositorio =
        FavoritosRepository(PokemonDatabase.obtener(application).favoritoDao())

    private val _estadoApi = MutableStateFlow(PokemonListUiState())

    val uiState: StateFlow<PokemonListUiState> = combine(
        _estadoApi,
        favoritosRepositorio.observarIds()
    ) { estadoApi, ids ->
        estadoApi.copy(favoritosIds = ids)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PokemonListUiState(cargando = true)
    )

    init { cargarMas() }

    fun cargarMas() {
        val estadoActual = _estadoApi.value
        if (estadoActual.cargando || !estadoActual.hayMas) return

        _estadoApi.update { it.copy(cargando = true, error = null) }

        viewModelScope.launch {
            val resultado = repositorio.obtenerPokemones(
                limite = TAMANO_PAGINA,
                desplazamiento = estadoActual.pokemones.size
            )
            when (resultado) {
                is Resultado.Exito -> _estadoApi.update {
                    it.copy(
                        pokemones = it.pokemones + resultado.datos,
                        cargando = false,
                        hayMas = resultado.datos.size == TAMANO_PAGINA
                    )
                }
                is Resultado.Error -> _estadoApi.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }

    fun alternarFavorito(pokemon: PokemonResumen) {
        val esFavorito = pokemon.id in uiState.value.favoritosIds
        viewModelScope.launch {
            if (esFavorito) favoritosRepositorio.quitar(pokemon.id)
            else favoritosRepositorio.agregar(pokemon)
        }
    }

    private companion object { const val TAMANO_PAGINA = 20 }
}
