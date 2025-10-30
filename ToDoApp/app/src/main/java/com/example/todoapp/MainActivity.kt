package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.theme.TodoappTheme
import org.intellij.lang.annotations.JdkConstants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoappTheme {
                Login()
            }
        }
    }
}

@Composable
fun Login() {

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "LogoApp",
            modifier = Modifier.size(200.dp)
                .padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = "", //Valor vacío
            onValueChange = {}, // aún sin nada al escribir
            label = { Text("Nombre") },
            singleLine = true, //una sola linea
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        TextField(
            value = "", //Valor vacío
            onValueChange = {}, // aún sin nada al escribir
            label = {
                Text("Alias")},
            singleLine = true, //una sola linea
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        )

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)

        ) {
            Text(
                "Continuar"
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoappTheme {
        Login()
    }
}