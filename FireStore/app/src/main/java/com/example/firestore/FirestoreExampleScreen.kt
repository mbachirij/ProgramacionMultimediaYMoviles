package com.example.firestore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun FirestoreExampleScreen() {

    val repo = remember { UsuarioRepositoryFirestore() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var listaUsuarios by remember { mutableStateOf(listOf<Usuario>()) }
    var mensaje by remember { mutableStateOf("") }

    var nombreActualizado by remember { mutableStateOf("") }
    var usuarioSeleccionado: Usuario? by remember { mutableStateOf(null) }

    LaunchedEffect(true) {
        scope.launch {
            listaUsuarios = repo.obtenerUsuarios()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("CRUD Firestore • DAM", style = MaterialTheme.typography.titleLarge)

        // ==========================
        // CREAR
        // ==========================
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Introduce un nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                scope.launch {
                    repo.insertarUsuario(nombre)
                    mensaje = "Usuario guardado"
                    nombre = ""
                    listaUsuarios = repo.obtenerUsuarios()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar usuario")
        }

        Divider()

        // ==========================
        // LISTA DE USUARIOS
        // ==========================
        listaUsuarios.forEach { usuario ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(usuario.nombre)

                Row {
                    // BOTÓN ACTUALIZAR
                    Button(onClick = {
                        usuarioSeleccionado = usuario
                        nombreActualizado = usuario.nombre
                    }) {
                        Text("Editar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // BOTÓN BORRAR
                    Button(onClick = {
                        scope.launch {
                            repo.borrarUsuario(usuario.id)
                            listaUsuarios = repo.obtenerUsuarios()
                        }
                    }) {
                        Text("Borrar")
                    }
                }
            }
        }

        Divider()

        // ==========================
        // DIÁLOGO DE ACTUALIZAR
        // ==========================
        usuarioSeleccionado?.let { usuarioEditar ->

            AlertDialog(
                onDismissRequest = { usuarioSeleccionado = null },
                title = { Text("Actualizar usuario") },
                text = {
                    TextField(
                        value = nombreActualizado,
                        onValueChange = { nombreActualizado = it },
                        label = { Text("Nuevo nombre") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            repo.actualizarUsuario(usuarioEditar.id, nombreActualizado)
                            listaUsuarios = repo.obtenerUsuarios()
                            usuarioSeleccionado = null
                        }
                    }) { Text("Guardar cambios") }
                },
                dismissButton = {
                    Button(onClick = { usuarioSeleccionado = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Text(mensaje)
    }
}
