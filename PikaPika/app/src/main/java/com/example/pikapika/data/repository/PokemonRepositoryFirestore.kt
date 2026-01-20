package com.example.pikapika.data.repository

import com.example.pikapika.data.model.Personaje
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PokemonRepositoryFirestore {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun coleccionPokemon() =
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("pokemon")

    suspend fun obtenerPokemon(): List<Personaje> {
        val snapshot = coleccionPokemon().get().await()
        return snapshot.documents.map { doc ->
            Personaje(
                id = doc.id,
                fecha = doc.getString("fecha") ?: "",

                // mapeamos los campos nuevos para los Pokemon
                pokemonNombre = doc.getString("pokemonNombre"),
                pokemonImagen = doc.getString("pokemonImagen"),
                pokemonTipo = doc.getString("pokemonTipo"),
                pokemonStats = doc.getString("pokemonStats")
            )
        }
    }

    suspend fun insertarPokemon(poke: Personaje) {
        val data = mapOf(
            "fecha" to poke.fecha,
            "pokemonNombre" to poke.pokemonNombre,
            "pokemonImagen" to poke.pokemonImagen,
            "pokemonTipo" to poke.pokemonTipo,
            "pokemonStats" to poke.pokemonStats
        )

        coleccionPokemon().add(data).await()
    }

    suspend fun borrarPokemon(id: String) {
        coleccionPokemon().document(id).delete().await()
    }
}