package com.letthepokemancook.data

interface CocktailApiService {
    suspend fun getRandomCocktail(): MealApiResponse

    companion object {
        fun create(): CocktailApiService {
            TODO("Not yet implemented")
        }
    }
}
