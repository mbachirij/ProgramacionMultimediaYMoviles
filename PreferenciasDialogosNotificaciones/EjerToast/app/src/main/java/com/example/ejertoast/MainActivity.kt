package com.example.ejertoast

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ejertoast.ui.theme.EjerToastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjerToastTheme {
                Tostada()
            }
        }
    }
}

@Composable
fun Tostada() {

    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("SHORT") }
    var contador by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .background(Color.Blue)

        ){
            Text("Toast en Android", color = Color.White)
        }

        Text("Texto",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)

        )

        TextField(
            value = texto,
            onValueChange = {texto = it},
            textStyle = TextStyle(
                fontSize = if (selectedOption == "LONG") 24.sp else 16.sp
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),

        )

        Text("Duración",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            RadioButton(
                selected = selectedOption == "LONG",
                onClick = {selectedOption = "LONG"},

            )
            Text("LENGTH_LONG")
            RadioButton(
                selected = selectedOption == "SHORT",
                onClick = { selectedOption = "SHORT"},
                )
            Text("LENGTH_SHORT")
        }

        Button(onClick = {
            Toast.makeText(context, texto, Toast.LENGTH_SHORT).apply {
                // Cambiar posición: arriba, centrado horizontalmente
                setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
                show()

            }
            contador++
        }) {
            Text("Mostrar Toast")
        }

        Text("# TOAST MOSTRADOS: $contador",
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
                .padding(end = 16.dp)
            )

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EjerToastTheme {
        Tostada()
    }
}