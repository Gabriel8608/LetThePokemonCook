package com.example.ltpc.data

data class PokemonResumen(
    val id: Int,
    val nombre: String,
    val imagenUrl: String
)

data class PokemonDetalle(
    val id: Int,
    val nombre: String,
    val imagenUrl: String,
    val tipos: List<String> = emptyList(),
    val peso: Int = 0,
    val altura: Int = 0
) {
    fun aResumen(): PokemonResumen = PokemonResumen(id = id, nombre = nombre, imagenUrl = imagenUrl)
}
