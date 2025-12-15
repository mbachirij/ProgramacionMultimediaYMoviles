package com.example.loginfirestore



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

object LoginAndWelcomeScreen {

    // -----------------------------------------------------
    // LOGIN SCREEN
    // -----------------------------------------------------
    @Composable
    fun LoginScreen(
        onLoginSuccess: (String) -> Unit,
        onNavigateToRegister: () -> Unit
    ) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var error by remember { mutableStateOf<String?>(null) }

        val auth = FirebaseAuth.getInstance()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") }
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        onLoginSuccess(result.user?.email ?: "Usuario")
                    }
                    .addOnFailureListener { e ->
                        error = e.localizedMessage
                    }
            }) {
                Text("Acceder")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Crear cuenta nueva")
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

    // -----------------------------------------------------
    // REGISTER SCREEN
    // -----------------------------------------------------
    @Composable
    fun RegisterScreen(
        onRegisterSuccess: (String) -> Unit,
        onBackToLogin: () -> Unit
    ) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var error by remember { mutableStateOf<String?>(null) }

        val auth = FirebaseAuth.getInstance()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña (mínimo 6 caracteres)") }
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        onRegisterSuccess(result.user?.email ?: "Usuario")
                    }
                    .addOnFailureListener { e ->
                        error = e.localizedMessage
                    }
            }) {
                Text("Registrarse")
            }

            Spacer(Modifier.height(8.dp))

            // ⬅️ NUEVO BOTÓN: VOLVER AL LOGIN
            TextButton(onClick = onBackToLogin) {
                Text("Volver al login")
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

    // -----------------------------------------------------
    // WELCOME SCREEN (con logout)
    // -----------------------------------------------------
    @Composable
    fun WelcomeScreen(
        username: String,
        onLogout: () -> Unit
    ) {

        val auth = FirebaseAuth.getInstance()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Bienvenido, $username", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(20.dp))

            // ⬅️ BOTÓN LOGOUT
            Button(onClick = {
                auth.signOut()
                onLogout()
            }) {
                Text("Cerrar sesión")
            }
        }
    }
}
