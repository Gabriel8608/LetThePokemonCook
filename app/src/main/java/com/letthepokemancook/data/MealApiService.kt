package com.letthepokemancook.data

interface MealApiService {
    suspend fun getRandomMeal(): MealApiResponse

    companion object {
        fun create(): MealApiService {
            TODO("Not yet implemented")
        }
    }
}
