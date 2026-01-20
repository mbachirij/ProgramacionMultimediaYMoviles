package com.example.pikapika.data.model

data class Personaje(
    val id: String = "",
    val fecha: String = "",

    // Campos nuevos para los Pokemon
    val pokemonNombre: String? = null,
    val pokemonImagen: String? = null,
    val pokemonTipo: String? = null,
    val pokemonStats: String? = null
)