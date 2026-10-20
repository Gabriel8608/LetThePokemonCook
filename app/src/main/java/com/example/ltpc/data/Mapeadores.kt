package com.example.ltpc.data

import com.example.ltpc.data.local.FavoritoEntity

fun FavoritoEntity.aResumen(): PokemonResumen =
    PokemonResumen(id = id, nombre = nombre, imagenUrl = imagenUrl)

fun PokemonResumen.aEntidad(): FavoritoEntity =
    FavoritoEntity(id = id, nombre = nombre, imagenUrl = imagenUrl)
