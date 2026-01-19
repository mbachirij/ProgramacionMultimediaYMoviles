package com.example.todoapp.data.repository

import com.example.todoapp.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TareaRepositoryFirestore {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun coleccionTareas() =
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("tareas")

    suspend fun obtenerTareas(): List<Tarea> {
        val snapshot = coleccionTareas().get().await()
        return snapshot.documents.map { doc ->
            Tarea(
                id = doc.id,
                tarea = doc.getString("tarea") ?: "",
                fecha = doc.getString("fecha") ?: "",

                // mapeamos los campos nuevos para los Pokemon
                pokemonNombre = doc.getString("pokemonNombre"),
                pokemonImagen = doc.getString("pokemonImagen"),
                pokemonTipo = doc.getString("pokemonTipo"),
                pokemonStats = doc.getString("pokemonStats")
            )
        }
    }

    suspend fun insertarTarea(tarea: Tarea) {
        val data = mapOf(
            "tarea" to tarea.tarea,
            "fecha" to tarea.fecha,
            "pokemonNombre" to tarea.pokemonNombre,
            "pokemonImagen" to tarea.pokemonImagen,
            "pokemonTipo" to tarea.pokemonTipo,
            "pokemonStats" to tarea.pokemonStats
        )

        coleccionTareas().add(data).await()
    }

    suspend fun borrarTarea(id: String) {
        coleccionTareas().document(id).delete().await()
    }
}