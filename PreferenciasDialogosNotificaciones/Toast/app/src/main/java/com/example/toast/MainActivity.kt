package com.example.toast

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.toast.ui.theme.ToastTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    ToastExample()
                }
            }
        }
    }
}
@Composable
fun ToastExample() {
    val context = LocalContext.current  // necesario para mostrar el Toast

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            Toast.makeText(context, "¡Hola! Esto es un Toast", Toast.LENGTH_SHORT).apply {
                // Cambiar posición: arriba, centrado horizontalmente
                setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
                show()
            }
        }) {
            Text("Mostrar Toast")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToastTheme {
        ToastExample()
    }
}