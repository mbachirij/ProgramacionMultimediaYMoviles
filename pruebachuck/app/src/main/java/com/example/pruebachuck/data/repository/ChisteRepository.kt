package com.example.pruebachuck.data.repository

import androidx.lifecycle.viewModelScope
import com.example.pruebachuck.data.local.ChisteDao
import com.example.pruebachuck.data.model.Chiste
import com.example.pruebachuck.data.model.ChisteEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Esta clase es para obtener los datos de Firestore
class ChisteRepository(private val dao: ChisteDao) {

    // esta variable es para obtener el usuario actual
    private val auth = FirebaseAuth.getInstance()
    // esta variable es para obtener la base de datos
    private val db = FirebaseFirestore.getInstance()


    private fun coleccionChistes() =
        auth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("chistes")
        }
    fun obtenerChistesLocal() = dao.obtenerChistes()

    suspend fun insertarChiste(entity: ChisteEntity) {

        // Guardar en local
        dao.insertarChiste(entity)

        val ref = coleccionChistes() ?: return

        val data = mapOf(
            "texto" to entity.texto,
            "iconoUrl" to entity.iconoUrl,
            "fecha_guardado" to System.currentTimeMillis()
        )

        ref.document(entity.id).set(data).await()
    }

    suspend fun borrarChiste(entity: ChisteEntity) {

        dao.borrarChiste(entity)

        val ref = coleccionChistes() ?: return
        ref.document(entity.id).delete().await()
    }

    suspend fun sincronizarChistes() {

        val ref = coleccionChistes() ?: return

        val snapshot = ref.get().await()

        snapshot.documents.forEach { doc ->
            val entity = ChisteEntity(
                id = doc.id,
                texto = doc.getString("texto") ?: "",
                iconoUrl = doc.getString("iconoUrl") ?: ""
            )
            dao.insertarChiste(entity)
        }
    }
}