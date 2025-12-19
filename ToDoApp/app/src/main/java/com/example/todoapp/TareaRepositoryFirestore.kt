package com.example.todoapp


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
                fecha = doc.getString("fecha") ?: ""
            )
        }
    }

    suspend fun insertarTarea(tarea: String, fecha: String) {
        val data = mapOf(
            "tarea" to tarea,
            "fecha" to fecha
        )
        coleccionTareas().add(data).await()
    }

    suspend fun borrarTarea(id: String) {
        coleccionTareas().document(id).delete().await()
    }
}
