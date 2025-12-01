package com.example.persistencia

import androidx.compose.ui.platform.LocalContext



import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.persistencia.ui.theme.PersistenciaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ArchivoScreen()
                }
            }
        }
    }
}

@Composable
fun ArchivoScreen() {
    val context = LocalContext.current
    var texto by remember { mutableStateOf("") }
    var contenidoLeido by remember { mutableStateOf("") }

    val nombreArchivo = "mensaje.txt"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Persistencia con openFileOutput / openFileInput",
            style = MaterialTheme.typography.titleLarge
        )

        TextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Escribe un texto") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- BOTÓN GUARDAR ---
        Button(
            onClick = {
                try {
                    // Abrimos el archivo en modo privado (se sobrescribe si ya existe)
                    context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use { output ->
                        output.write(texto.toByteArray())
                    }
                    contenidoLeido = "Texto guardado correctamente."
                } catch (e: Exception) {
                    contenidoLeido = "Error al guardar: ${e.message}"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar texto")
        }

        // --- BOTÓN LEER ---
        Button(
            onClick = {
                try {
                    val input = context.openFileInput(nombreArchivo)
                    val contenido = input.bufferedReader().use { it.readText() }
                    input.close()
                    contenidoLeido = contenido
                } catch (e: Exception) {
                    contenidoLeido = "Error al leer: ${e.message}"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Leer archivo")
        }

        // --- BOTÓN BORRAR ---
        Button(
            onClick = {
                val deleted = context.deleteFile(nombreArchivo)
                contenidoLeido = if (deleted) {
                    "Archivo eliminado correctamente."
                } else {
                    "No se encontró el archivo para eliminar."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar archivo")
        }

        Text(
            text = contenidoLeido,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PersistenciaTheme {
        ArchivoScreen()
    }
}