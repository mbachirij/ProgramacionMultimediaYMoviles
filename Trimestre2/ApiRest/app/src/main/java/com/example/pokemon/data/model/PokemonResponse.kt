package com.example.pokemon.data.model



data class PokemonResponse(
    val results: List<PokemonItem>
)

data class PokemonItem(
    val name: String,
    val url: String
) {
    // Obtiene el ID desde la URL
    fun getId(): String {
        return url.trimEnd('/').split("/").last()
    }

    // Construye la URL de la imagen del sprite
    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getId()}.png"
    }
}
