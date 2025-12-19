package com.example.todoapp

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Registro(onVolverLogin: () -> Unit){

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text("Email")
            }
        )
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = {
                Text("Contraseña")
            }
        )

        Button(onClick = {
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    Toast.makeText(context, "Cuenta creada", Toast.LENGTH_SHORT).show()
                    onVolverLogin()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Registrarse")
        }
    }
}
