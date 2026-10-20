package com.example.ltpc.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    // El id del pokemon ya es unico en la API: lo usamos como clave primaria.
    @PrimaryKey val id: Int,
    val nombre: String,
    val imagenUrl: String,
    // Momento en que se marco como favorito; sirve para ordenar.
    val agregadoEn: Long = System.currentTimeMillis()
)
