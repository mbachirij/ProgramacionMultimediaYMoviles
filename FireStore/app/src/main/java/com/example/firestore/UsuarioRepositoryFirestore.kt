package com.example.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsuarioRepositoryFirestore {

    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("usuarios")

    // ----------------------
    // INSERTAR
    // ----------------------
    suspend fun insertarUsuario(nombre: String) {
        val nuevo = mapOf("nombre" to nombre)
        coleccion.add(nuevo).await()
    }

    // ----------------------
    // LEER TODOS
    // ----------------------
    suspend fun obtenerUsuarios(): List<Usuario> {
        val snapshot = coleccion.get().await()

        return snapshot.documents.map { doc ->
            Usuario(
                id = doc.id,
                nombre = doc.getString("nombre") ?: ""
            )
        }
    }

    // ----------------------
    // ACTUALIZAR
    // ----------------------
    suspend fun actualizarUsuario(id: String, nuevoNombre: String) {
        coleccion.document(id)
            .update("nombre", nuevoNombre)
            .await()
    }

    // ----------------------
    // BORRAR
    // ----------------------
    suspend fun borrarUsuario(id: String) {
        coleccion.document(id)
            .delete()
            .await()
    }
}
