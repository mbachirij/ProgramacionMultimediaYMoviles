package com.example.pokemon.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.example.pokemon.ui.PokemonViewModel

@Composable
fun PokemonScreen(viewModel: PokemonViewModel) {

    val pokemons by viewModel.pokemonList.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Pokédex", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(pokemons) { p ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        AsyncImage(
                            model = p.getImageUrl(),
                            contentDescription = p.name,
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = p.name.uppercase())
                    }
                }
            }
        }
    }
}
