package com.example.pokemon.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.example.pokemon.ui.PokemonViewModel

@Composable
fun PokemonScreen(viewModel: PokemonViewModel) {

    val pokemons by viewModel.pokemonList.collectAsState()

    val verMas by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Pokédex", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Define 2 columnas
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(pokemons) { p ->

                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Card(
                        Modifier.size(width = 220.dp, height = 140.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            AsyncImage(
                                 model = p.getImageUrl(),
                                 contentDescription = p.name,
                                 modifier = Modifier.size(80.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = p.name.uppercase())
                        }

                        Spacer(modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp))
                    }
                }
            }
        }
    }
}
