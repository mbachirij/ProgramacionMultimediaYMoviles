package com.example.todoapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.theme.TodoappTheme
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants
import java.util.jar.Manifest

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
    var preferencias by remember { mutableStateOf(false) }
    var mostrarpreferencias by remember { mutableStateOf(false) }
    var colorText by remember { mutableStateOf(Color.Yellow) }
    var modOscuro by remember { mutableStateOf(false) }

    var textobusqueda by remember { mutableStateOf("") }
    var mostrardialogoborrar by remember { mutableStateOf(false) }
    var tareaborrar by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Notificación: Cuenta 3 minutos y avisa
    LaunchedEffect(listaTareas.size) {
        if (listaTareas.isNotEmpty()) {
            delay(10000L) // 3 minutos (180.000 milisegundos)

            val channelId = "canal_inactividad"
            val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Inactividad", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }


            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Recordatorio")
                .setContentText("Hace rato que no añades ninguna tarea")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            try {

                notificationManager.notify(1, builder.build())

            } catch (e: Exception) {

            }
        }
    }


    if (mostrarpreferencias){
        AlertDialog(
            onDismissRequest = { mostrarpreferencias = false },
            title = { Text("Preferencias color texto") },
            text = {
                Column {
                    Text("Color de las tareas ", fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically){
                        RadioButton(selected = colorText == Color.Red,
                            onClick = { colorText = Color.Red })
                        Text("Rojo")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        RadioButton(selected = colorText == Color.Blue,
                            onClick = { colorText = Color.Blue })
                        Text("Azul")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        RadioButton(selected = colorText == Color.Yellow,
                            onClick = { colorText = Color.Yellow })
                        Text("Amarillo")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Switch(checked = modOscuro,
                            onCheckedChange = { modOscuro = it})
                        Text("Modo Oscuro")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {mostrarpreferencias = false}) { Text("Aceptar") }
            }
        )
    }

    if (mostrardialogoborrar) {
        AlertDialog(
            onDismissRequest = { mostrardialogoborrar = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que quieres borrar la tarea '$tareaborrar'?") },
            confirmButton = {
                Button(onClick = {
                    listaTareas.remove(tareaborrar) // Aquí borramos de verdad
                    mostrardialogoborrar = false
                }) { Text("Sí, borrar") }
            },
            dismissButton = {
                Button(onClick = { mostrardialogoborrar = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( if (modOscuro) Color.LightGray else Color.White ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                "Hola, $globalNombre ($globalAlias)",
                color = Color.Black,
                fontSize = 25.sp
            )

            IconButton(onClick = { preferencias = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Opciones",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                DropdownMenu(
                    expanded = preferencias,
                    onDismissRequest = { preferencias = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("change color") },
                        onClick = {
                            preferencias = false
                            mostrarpreferencias = true
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Barra de Búsqueda
        OutlinedTextField(
            value = textobusqueda,
            onValueChange = { textobusqueda = it },
            label = { Text("Buscar tarea...") },
            leadingIcon = {
                Icon(Icons.Filled.Search,
                    contentDescription = null)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

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
            ) { Text("Añadir") }

        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val listaParaMostrar =
                if (textobusqueda.isEmpty())
                    listaTareas
                else
                    listaTareas.filter {
                        it.contains(textobusqueda, ignoreCase = true) }

            items(listaParaMostrar) { nuevaTarea ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        nuevaTarea,
                        modifier = Modifier.weight(1f),
                        color = colorText,
                        fontSize = 30.sp
                    )

                    IconButton(onClick = {
                        tareaborrar = nuevaTarea
                        mostrardialogoborrar = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
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