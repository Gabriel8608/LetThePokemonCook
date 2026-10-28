package com.example.ltpc.data.repository

import com.example.ltpc.data.local.FavoritoDao
import com.example.ltpc.domain.PokemonResumen
import com.example.ltpc.domain.aEntidad
import com.example.ltpc.domain.aResumen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritosRepository(private val dao: FavoritoDao) {

    fun observarFavoritos(): Flow<List<PokemonResumen>> =
        dao.observarFavoritos().map { entidades -> entidades.map { it.aResumen() } }

    fun observarIds(): Flow<Set<Int>> =
        dao.observarIdsFavoritos().map { ids -> ids.toSet() }

    suspend fun agregar(pokemon: PokemonResumen) = dao.insertar(pokemon.aEntidad())

    suspend fun quitar(id: Int) = dao.eliminarPorId(id)
}
