package com.example.todoapp.data.repository

import com.example.todoapp.data.network.PokemonApi

class PokemonRepository(private val api: PokemonApi) {

    suspend fun getPokemons() = api.getPokemons()
}
