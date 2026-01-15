package com.example.pokemon.data.repository

import com.example.pokemon.data.network.PokemonApi

class PokemonRepository(private val api: PokemonApi) {

    suspend fun getPokemons() = api.getPokemons()
}
