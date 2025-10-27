package com.example.judagores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.judagores.ui.theme.JudagoresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JudagoresTheme {
                DropdownJugadores()
            }
        }
    }
}

@Composable
fun DropdownJugadores() {
    var expanded by remember { mutableStateOf(false) }

    val jugadores = listOf("Messi", "Cristiano", "Mbappé", "Neymar", "Modric")
    val seleccionados = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) //  Fondo gris muy claro
            .padding(top = 100.dp, start = 24.dp, end = 24.dp), //  Baja la lista
        verticalArrangement = Arrangement.Top
    ) {
        // Caja para abrir el menú
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2), shape = RoundedCornerShape(10.dp)) // 👈 Azul intenso
                .clickable { expanded = true }
                .padding(14.dp)
        ) {
            Text(
                text = if (seleccionados.isEmpty()) "Seleccionar jugadores"
                else "Aficionados: ${seleccionados.size}",
                fontSize = 16.sp,
                color = Color.White // texto blanco sobre azul
            )
        }

        // Menú desplegable
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            jugadores.forEach { jugador ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = jugador in seleccionados,
                                onCheckedChange = { marcado ->
                                    if (marcado) seleccionados.add(jugador)
                                    else seleccionados.remove(jugador)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF388E3C), // verde fuerte
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(jugador, color = Color(0xFF212121)) // negro suave
                        }
                    },
                    onClick = { /* vacío porque usamos el CheckBox */ }
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        // Mostrar los seleccionados
        Text(
            text = if (seleccionados.isEmpty()) "Ningún jugador seleccionado"
            else "Seleccionados: ${seleccionados.joinToString()}",
            fontSize = 16.sp,
            color = Color(0xFFD32F2F), // rojo vivo
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JudagoresTheme {
        DropdownJugadores()
    }
}