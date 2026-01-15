package com.example.pokemon.data.network

import com.example.pokemon.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int = 20
    ): PokemonResponse
}
