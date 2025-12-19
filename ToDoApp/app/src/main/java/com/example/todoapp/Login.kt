package com.example.todoapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(onIrPantalla2: () -> Unit, onIrRegistro: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF50E0E0E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {
            if (email.isBlank() || contrasena.isBlank()) {
                Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@Button
            }
                auth.signInWithEmailAndPassword(email, contrasena)

                    .addOnSuccessListener {
                        onIrPantalla2()
                    }

                    .addOnFailureListener {
                        Toast.makeText(context, "Usuario no registrado", Toast.LENGTH_SHORT).show()
                    }

        }) {
            Text("Iniciar sesión")
        }

        TextButton(onClick = onIrRegistro) {
            Text("¿No tienes cuenta? Regístrate", color = Color.Blue)
        }
    }
}
