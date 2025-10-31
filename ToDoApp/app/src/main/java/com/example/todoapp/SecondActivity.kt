package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.theme.TodoappTheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoappTheme {
                SecondScreen()
            }
        }
    }
}

@Composable
fun SecondScreen() {
    Text(text = "Bienvenido a la segunda pantalla")
}

@Preview(showBackground = true)
@Composable
fun SecondPreview() {
    TodoappTheme {
        Login()
    }
}