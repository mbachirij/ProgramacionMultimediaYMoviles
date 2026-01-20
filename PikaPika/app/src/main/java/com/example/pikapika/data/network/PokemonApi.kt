package com.example.pikapika.data.network

import androidx.compose.ui.graphics.vector.Path
import com.example.pikapika.data.model.Pokemon
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Path

// Aquí defino que pido un pokemon concreto por nombre a la API
interface PokemonApi {
    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Pokemon
}

// aquí creamos la conexion con la API
object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val api: PokemonApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }
}