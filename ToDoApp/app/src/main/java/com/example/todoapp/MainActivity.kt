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
// --- VARIABLES GLOBALES ---
// Las pongo fuera para poder acceder a ellas desde cualquier pantalla
// Son para guardar el nombre y alias del Login y mostrarlos en la Pantalla 2
var globalNombre = ""
var globalAlias = ""

@Composable
fun TodoApp(){
    // variable navController para gestionar la navegación
    var navController = rememberNavController()

    // Aquí definimos las pantallas de la app y sus rutas
    NavHost(
        navController = navController,
        startDestination = "login" // Es la primera pantalla que se ve
    ){
        composable("login") {
            // Le pasamos la funcion para navegar a la siguiente pantalla
            Login(onIrPantalla2 = { navController.navigate("pantalla2")})
        }
        composable("pantalla2") {
            Pantalla2()
        }
    }
}


// Pantalla 1: LOGIN
@Composable
fun Login(onIrPantalla2: () -> Unit) {
    // Estados para los campos de texto
    var nombre by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo de la app (imagen en la carpeta drawable)
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "LogoApp",
            modifier = Modifier.size(200.dp)
                .padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
        // Campo nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it},
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // Campo Alias
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
        // Botón continuar con validación
        Button(
            onClick = {
                // If para verificar que los campos nombre y alias no están vaciós
                if (nombre.isNotBlank() && alias.isNotBlank()){
                    globalNombre = nombre // guardo en variable global
                    globalAlias = alias
                    onIrPantalla2() // navegamos a pantalla2
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Continuar")
        }

    }

}


@Composable
fun Pantalla2() {

    var nuevaTarea by remember { mutableStateOf("") }
    // MutableStateOf permite que la lista se actualice en pantalla al añadir/borrar
    val listaTareas = remember { mutableStateListOf<String>() }

    // Estados para Preferencias
    // Controla el menú desplegable
    var preferencias by remember { mutableStateOf(false) }
    // Controla el diálogo de ajustes
    var mostrarpreferencias by remember { mutableStateOf(false) }
    // Color del texto por defecto amarillo
    var colorText by remember { mutableStateOf(Color.Yellow) }
    // Switch modo oscuro
    var modOscuro by remember { mutableStateOf(false) }

    // Buscador
    var textobusqueda by remember { mutableStateOf("") }
    // Diálogo borrar
    var mostrardialogoborrar by remember { mutableStateOf(false) }
    // Tarea temporal a borrar
    var tareaborrar by remember { mutableStateOf("") }

    // Contexto necesario para notificaciones
    val context = LocalContext.current

    // (LaunchedEffect)
    // Notificación: que Cuenta 3 minutos y avisa
    LaunchedEffect(listaTareas.size) {
        if (listaTareas.isNotEmpty()) {
            delay(10000L) // 3 minutos (180.000 milisegundos)

            // Configuración obligatoria para Android8.0+ (Canales)
            val channelId = "canal_inactividad"
            val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Inactividad", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }

            // Construcción de la notificación
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Recordatorio")
                .setContentText("Hace rato que no añades ninguna tarea")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            try {
                notificationManager.notify(1, builder.build())
            } catch (e: Exception) {
                // Capturamos el error si falta el permiso POST_NOTIFICATIONS
            }
        }
    }

    // Diálogo de Preferencias
    if (mostrarpreferencias){
        AlertDialog(
            onDismissRequest = { mostrarpreferencias = false },
            title = { Text("Preferencias color texto") },
            text = {
                Column {
                    Text("Color de las tareas ", fontWeight = FontWeight.Bold)
                    // Opciones de color
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
                    // Switch Modo Oscuro
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Switch(checked = modOscuro,
                            onCheckedChange = { modOscuro = it})
                        Text("Modo Oscuro")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { mostrarpreferencias = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
    // Diálogo de Confirmación de Borrado
    if (mostrardialogoborrar) {
        AlertDialog(
            // onDismissRequest para cerrar el diálogo mostrardialogo = false
            onDismissRequest = { mostrardialogoborrar = false },
            // Título del diálogo
            title = { Text("Confirmar eliminación") },
            // Pregunta del diálogo
            text = { Text("¿Seguro que quieres borrar la tarea '$tareaborrar'?") },
            confirmButton = {
                Button(onClick = {
                    // Aquí ocurre el borrado!!!
                    listaTareas.remove(tareaborrar) // Aquí borramos de verdad
                    mostrardialogoborrar = false
                }) { Text("Sí, borrar") }
            },
            dismissButton = {
                Button(onClick = { mostrardialogoborrar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    /*
     * CONFIGURACIÓN PANTALLA PRINCIPAL DE PANTALLA 2
     */
    Column(
        modifier = Modifier
            .fillMaxSize()
            // Aplicamos fondo dinámico según el switch de modo oscuro
            .background( if (modOscuro) Color.LightGray else Color.White ),
    ) {
        // Cabecera con Saludo y Menú de Opciones
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
            // Botón de opciones (3 puntos)
            IconButton(onClick = { preferencias = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Opciones",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                // ----- DROPDOWNMENU -----
                DropdownMenu(
                    expanded = preferencias,
                    // Es para cuando pulses fuera del menú se cierre
                    onDismissRequest = { preferencias = false }
                ) {
                    // DropdownMenuItem para cada opción del menu
                    DropdownMenuItem(
                        text = { Text("Preferencias") },
                        onClick = {
                            preferencias = false // Cerrar el menú de preferencias
                            mostrarpreferencias = true // Abrimos el diálogo de preferencias
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Barra de Búsqueda
        OutlinedTextField(
            value = textobusqueda,
            // Filtra automáticamente al escribir
            onValueChange = { textobusqueda = it },
            // Contenido del TextField
            label = { Text("Buscar tarea...") },
            // Icono de búsqueda
            leadingIcon = {
                Icon(Icons.Filled.Search,
                    contentDescription = null)
            },
            // Permite una sola línea
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Añadir tarea
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            // Campo de texto para añadir tarea
            OutlinedTextField(
                value = nuevaTarea,
                onValueChange = { nuevaTarea = it },
                label = {
                    Text("Nueva tarea")},
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
            )
            // Botón Añadir
            Button(
                onClick = {
                    // Si no está vacío se añade a la lista
                    if (nuevaTarea.isNotBlank()){
                        listaTareas.add(nuevaTarea) // lo añadimos a la lista
                        nuevaTarea = "" // limpiamos el campo de texto
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) { Text("Añadir") }

        }
        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn para mostrar la lista de tareas
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /*
             * LÓGICA DE FILTRADO
             * Si el buscador está vacío, mostramos todo.
             * Si no, filtramos por coincidencias.
             */
            val listaParaMostrar =
                if (textobusqueda.isEmpty())
                    listaTareas
                else
                    listaTareas.filter {
                        it.contains(textobusqueda, ignoreCase = true) }
            // Recorremos la lista de tareas
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
                    // Botón Papeleras para borrar tarea
                    IconButton(onClick = {
                        // Guardamos la tarea a borrar en una variable temporal
                        tareaborrar = nuevaTarea
                        // Mostramos el diálogo de confirmación
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
                // Separador entre tareas
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