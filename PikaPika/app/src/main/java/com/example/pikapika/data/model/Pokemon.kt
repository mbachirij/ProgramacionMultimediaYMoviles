package com.example.pikapika.data.model

// Esto es para que la app entienda el JSON que manda la API
data class Pokemon(
    val name: String,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<StatEntry>
)

data class Sprites(val front_default: String?)
data class TypeSlot(val type: TypeName)
data class TypeName(val name: String)
data class StatEntry(
    val base_stat: Int,
    val stat: StatName
)
data class StatName(val name: String)