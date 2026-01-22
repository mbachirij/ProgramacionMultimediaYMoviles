package com.example.pruebachuck.data.repository

import com.example.pruebachuck.data.model.Chiste
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Esta clase es para obtener los datos de Firestore
class ChisteRepositoryFirestore {

    // esta variable es para obtener el usuario actual
    private val auth = FirebaseAuth.getInstance()
    // esta variable es para obtener la base de datos
    private val db = FirebaseFirestore.getInstance()


    private fun coleccionChistes() =
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("chistes")

    suspend fun obtenerChistes(): List<Chiste> {
        // Pido los datos y espero (await)
        val snapshot = coleccionChistes().get().await()

        // Aquí convierto cada documento de Firebase en un objeto Chiste
        return snapshot.documents.map { doc ->
            Chiste(
                id = doc.id,
                texto = doc.getString("value") ?: "",
                iconoUrl = doc.getString("icon_url") ?: ""
            )
        }
    }

    suspend fun insertarChiste(chiste: Chiste) {
        val data = mapOf(
            "texto" to chiste.texto,
            "iconoUrl" to chiste.iconoUrl,
            "fecha_guardado" to System.currentTimeMillis() // Aquí guardos la fecha actual
        )
        // esto haría duplicados
        // coleccionChistes().add(data).await()
        // esto no porque usa la id del chiste como nombre
        coleccionChistes().document(chiste.id).set(data).await()
    }

    suspend fun borrarChiste(id: String) {
        coleccionChistes().document(id).delete().await()
    }
}