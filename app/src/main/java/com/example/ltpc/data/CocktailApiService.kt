package com.example.ltpc.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CocktailApiService {
    @GET("random.php")
    suspend fun getRandomCocktail(): MealApiResponse

    companion object {
        fun create(): CocktailApiService {
            return Retrofit.Builder()
                .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CocktailApiService::class.java)
        }
    }
}
