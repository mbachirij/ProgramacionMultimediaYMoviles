package com.example.pokemon.ui



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.data.model.PokemonItem
import com.example.pokemon.data.network.PokemonApi
import com.example.pokemon.data.repository.PokemonRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonViewModel : ViewModel() {

    private val api = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")   // sin barra final
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApi::class.java)

    private val repo = PokemonRepository(api)  

    private val _pokemonList = MutableStateFlow<List<PokemonItem>>(emptyList())
    val pokemonList: StateFlow<List<PokemonItem>> = _pokemonList

    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            try {
                val response = repo.getPokemons()
                _pokemonList.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
                _pokemonList.value = emptyList()   // ← evita crash
            }
        }
    }
}