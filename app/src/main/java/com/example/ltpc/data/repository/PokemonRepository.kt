package com.example.ltpc.data.repository

import com.example.ltpc.data.remote.PokeApiService
import com.example.ltpc.domain.PokemonDetalle
import com.example.ltpc.domain.PokemonResumen
import com.example.ltpc.util.Resultado

class PokemonRepository(private val apiService: PokeApiService = PokeApiService.create()) {

    suspend fun obtenerPokemones(limite: Int = 20, desplazamiento: Int = 0): Resultado<List<PokemonResumen>> {
        return try {
            val lista = (1..limite).map { index ->
                val id = desplazamiento + index
                val response = apiService.getPokemon(id)
                PokemonResumen(
                    id = id,
                    nombre = response.name.replaceFirstChar { it.uppercase() },
                    imagenUrl = response.sprites.front_default ?: ""
                )
            }
            Resultado.Exito(lista)
        } catch (e: Exception) {
            Resultado.Error("Error al cargar los Pokémon: ${e.localizedMessage}", e)
        }
    }

    suspend fun obtenerDetalle(nombreOId: String): Resultado<PokemonDetalle> {
        return try {
            val id = nombreOId.toIntOrNull()
            val response = if (id != null) {
                apiService.getPokemon(id)
            } else {
                apiService.getPokemon((1..1000).random())
            }
            val detalle = PokemonDetalle(
                id = id ?: (1..1000).random(),
                nombre = response.name.replaceFirstChar { it.uppercase() },
                imagenUrl = response.sprites.front_default ?: ""
            )
            Resultado.Exito(detalle)
        } catch (e: Exception) {
            Resultado.Error("Error al cargar el detalle: ${e.localizedMessage}", e)
        }
    }
}
