package com.example.pruebachuck.data.network

import com.example.pruebachuck.data.model.Chiste
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Query

// Aquí defino que pido un pokemon concreto por nombre a la API
interface ChisteApi {
    // Pedir un chiste por categoría
    @GET("jokes/random")
    suspend fun getChisteCategoria(@Query("category") categoria: String): Chiste

    // Pedir un chiste aleatorio
    @GET("jokes/random")
    suspend fun getChisteAleatorio(): Chiste

    // Petición get para listar categorias
    @GET("jokes/categories")
    suspend fun getCategorias(): List<String>
}

// aquí creamos la conexion con la API
object RetrofitClient {
    private const val BASE_URL = "https://api.chucknorris.io/"

    val api: ChisteApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChisteApi::class.java)
    }
}