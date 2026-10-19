package com.letthepokemancook.data

interface PokeApiService {
    suspend fun getPokemon(id: Int): PokemonApiResponse

    companion object {
        fun create(): PokeApiService {
            TODO("Not yet implemented")
        }
    }
}
