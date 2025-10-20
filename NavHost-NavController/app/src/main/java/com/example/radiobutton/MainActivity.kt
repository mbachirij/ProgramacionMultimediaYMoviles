package com.example.radiobutton

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radiobutton.ui.theme.RadioButtonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RadioButtonTheme {
                Pantalla1()
            }
        }
    }
}



@Composable
fun Pantalla1() {
    val context = LocalContext.current

    // Estado para el RadioButton
    var selectedOption by remember { mutableStateOf("") } // o Int, según prefieras

    Box(
        modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
    ){

    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOption == "Red",
                onClick = { selectedOption = "Red" }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Red")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOption == "Green",
                onClick = { selectedOption = "Green" }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Green")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOption == "Blue",
                onClick = { selectedOption = "Blue" }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Blue")
        }

        Button(onClick = {
            if (selectedOption.isNotEmpty()) {
                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra("color", selectedOption)  // Enviamos el color
                context.startActivity(intent)
            }
        }) {
            Text("Ir a SecondActivity")
        }

    }



}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RadioButtonTheme {
        Pantalla1()
    }
}