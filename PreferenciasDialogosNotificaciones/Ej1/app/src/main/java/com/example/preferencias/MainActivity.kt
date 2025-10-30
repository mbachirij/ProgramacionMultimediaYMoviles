package com.example.preferencias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Leemos el valor guardado
    val selectedColor = SettingsPreferences.getColorMode(context).collectAsState(initial = "Rojo")


    val colores = listOf("Rojo", "Verde", "Azul")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = if (colorMode.value) "Texto verde ACTIVADO" else "Texto amarillo ACTIVADO",
            color = if (colorMode.value) Color.Green else Color.Yellow,
            style = MaterialTheme.typography.headlineMedium
        )

        Switch(
            checked = colorMode.value,
            onCheckedChange = { checked ->
                scope.launch {
                    SettingsPreferences.setColorMode(context, checked)
                }
            }
        )
    }
}