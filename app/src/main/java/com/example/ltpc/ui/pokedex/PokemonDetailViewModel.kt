package com.example.ltpc.ui.pokedex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ltpc.data.FavoritosRepository
import com.example.ltpc.data.PokemonDetalle
import com.example.ltpc.data.PokemonRepository
import com.example.ltpc.data.local.PokemonDatabase
import com.example.ltpc.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PokemonDetailUiState(
    val detalle: PokemonDetalle? = null,
    val cargando: Boolean = true,
    val error: String? = null,
    val esFavorito: Boolean = false
)

class PokemonDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repositorio = PokemonRepository()
    private val favoritosRepositorio =
        FavoritosRepository(PokemonDatabase.obtener(application).favoritoDao())

    private val nombre: String = savedStateHandle.get<String>(ARG_NOMBRE) ?: "1"

    private val _estadoApi = MutableStateFlow(PokemonDetailUiState())

    val uiState: StateFlow<PokemonDetailUiState> = combine(
        _estadoApi,
        favoritosRepositorio.observarIds()
    ) { estadoApi, ids ->
        val id = estadoApi.detalle?.id
        estadoApi.copy(esFavorito = id != null && id in ids)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PokemonDetailUiState()
    )

    init { cargar() }

    fun cargar() {
        _estadoApi.value = PokemonDetailUiState(cargando = true)
        viewModelScope.launch {
            when (val resultado = repositorio.obtenerDetalle(nombre)) {
                is Resultado.Exito -> _estadoApi.value = PokemonDetailUiState(
                    detalle = resultado.datos,
                    cargando = false
                )
                is Resultado.Error -> _estadoApi.value = PokemonDetailUiState(
                    cargando = false,
                    error = resultado.mensaje
                )
            }
        }
    }

    fun alternarFavorito() {
        val estado = uiState.value
        val detalle = estado.detalle ?: return
        viewModelScope.launch {
            if (estado.esFavorito) favoritosRepositorio.quitar(detalle.id)
            else favoritosRepositorio.agregar(detalle.aResumen())
        }
    }

    companion object { const val ARG_NOMBRE = "nombre" }
}
