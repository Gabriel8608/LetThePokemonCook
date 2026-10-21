package com.example.ltpc.util

sealed interface Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>
    data class Error(val mensaje: String, val causa: Throwable? = null) : Resultado<Nothing>
}
