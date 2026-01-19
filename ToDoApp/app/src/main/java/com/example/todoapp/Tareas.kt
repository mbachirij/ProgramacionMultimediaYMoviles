package com.example.todoapp

data class Tarea(

    val id: String = "",
    val fecha: String = "",
    val tarea: String = "",

    // Campos nuevos para los Pokemon
    val pokemonNombre: String? = null,
    val pokemonImagen: String? = null,
    val pokemonTipo: String? = null,
    val pokemonStats: String? = null
)



