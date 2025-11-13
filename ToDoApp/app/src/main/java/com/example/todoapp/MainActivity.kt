package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.theme.TodoappTheme
import org.intellij.lang.annotations.JdkConstants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoappTheme {
                TodoApp()
            }
        }
    }
}

var globalNombre = ""
var globalAlias = ""

@Composable
fun TodoApp(){

    var navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"

    ){
        composable("login") {

            Login(onIrPantalla2 = { navController.navigate("pantalla2")})

        }
        composable("pantalla2") {
            Pantalla2()
        }
    }
}



@Composable
fun Login(onIrPantalla2: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }

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

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it},
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        OutlinedTextField(
            value = alias,
            onValueChange = { alias = it },
            label = {
                Text("Alias")},
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        )

        Button(
            onClick = {
                if (nombre.isNotBlank() && alias.isNotBlank()){
                    globalNombre = nombre
                    globalAlias = alias
                    onIrPantalla2()
                }
                      },
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)

        ) {
            Text("Continuar")
        }

    }

}



@Composable
fun Pantalla2() {

    var nuevaTarea by remember { mutableStateOf("") }
    val listaTareas = remember { mutableStateListOf<String>() }
    var dropd by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color(0xFFE3F2FD)),

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                "Hola, $globalNombre ($globalAlias)",
                color = Color.Black
            )

            IconButton(onClick = { dropd = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Opciones",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                DropdownMenu(
                    expanded = dropd,
                    onDismissRequest = { dropd = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("¿¿Ajustes??") },
                        onClick = {
                            dropd = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            OutlinedTextField(
                value = nuevaTarea,
                onValueChange = { nuevaTarea = it },
                label = {
                    Text("Nueva tarea")},
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
            )



            Button(
                onClick = {
                    if (nuevaTarea.isNotBlank()){
                        listaTareas.add(nuevaTarea)
                        nuevaTarea = ""
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)



            ) {
                Text(
                    "Añadir"
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listaTareas.forEach { nuevaTarea ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(nuevaTarea,
                        modifier = Modifier.weight(1f))



                    IconButton(onClick = { listaTareas.remove(nuevaTarea) }) {

                        Icon(
                            imageVector = Icons.Filled.RestoreFromTrash,
                            contentDescription = "Eliminar",
                            tint = Color.Black,

                            modifier = Modifier
                                .size(24.dp)
                        ) } }
                }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoappTheme {
        Pantalla2()
    }
}