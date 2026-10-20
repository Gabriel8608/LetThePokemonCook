package com.example.ltpc.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritoEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun favoritoDao(): FavoritoDao

    companion object {
        @Volatile
        private var instancia: PokemonDatabase? = null

        // Singleton: una sola instancia de la base de datos en toda la app.
        fun obtener(context: Context): PokemonDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokedex.db"
                ).build().also { instancia = it }
            }
    }
}
