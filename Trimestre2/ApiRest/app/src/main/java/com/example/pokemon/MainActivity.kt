package com.example.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.pokemon.ui.PokemonViewModel
import com.example.pokemon.ui.screen.PokemonScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel = PokemonViewModel()
                PokemonScreen(viewModel)
            }
        }
    }
}

