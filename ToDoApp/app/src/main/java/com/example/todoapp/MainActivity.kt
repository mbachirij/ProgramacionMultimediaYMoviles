package com.example.todoapp

import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.room.Room
import com.example.todoapp.ui.theme.TodoappTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants
import java.util.Calendar
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
    // Contexto necesario para notificaciones
    val context = LocalContext.current

    // Scope para ejecutar operaciones de base de datos (suspend fun)
    val scope = rememberCoroutineScope()

    // CREAR INSTANCIA DE LA BASE DE DATOS
    val db = remember { Room.databaseBuilder(context, AppDatabase::class.java, "tareas_room.db").build() }
    val dao = db.tareaDao()

    // --- TAREAS TEXTO/FECHA ---
    var nuevaTareaTexto by remember { mutableStateOf("") }
    var nuevaTareaFecha by remember { mutableStateOf("") }


    // --- LISTA GUARDA OBJETOS (Tarea) ---
    val listaTareas = remember { mutableStateListOf<Tarea>() }

    // Estados para Preferencias
    var preferencias by remember { mutableStateOf(false) } // Controla el menú desplegable
    var mostrarpreferencias by remember { mutableStateOf(false) } // Controla el diálogo de ajustes
    var colorText by remember { mutableStateOf(Color.Black) } // Color del texto por defecto NEGRO
    var modOscuro by remember { mutableStateOf(false) } // Switch modo oscuro

    // --- BUSCADOR Y BORRADO ---
    var textobusqueda by remember { mutableStateOf("") }
    var mostrardialogoborrar by remember { mutableStateOf(false) } // Diálogo borrar
    var tareaborrar by remember { mutableStateOf<Tarea?>(null) } // Objeto Tarea temporal a borrar ahora null


    LaunchedEffect(true) {
        listaTareas.clear()
        listaTareas.addAll(dao.obtenerTareas())
    }


    // (LaunchedEffect)
    // Notificación: que Cuenta 3 minutos y avisa
    LaunchedEffect(listaTareas.size) {

        delay(180000L) // 3 minutos

        val channelId = "canal_inactividad"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Inactividad",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Recordatorio")
            .setContentText("Hace rato que no añades ninguna tarea")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        try {
            notificationManager.notify(1, builder.build())
        } catch (_: Exception) {}
    }

    // --- DATE PICKER ---
    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            nuevaTareaFecha = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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
    if (mostrardialogoborrar && tareaborrar != null) {
        AlertDialog(
            onDismissRequest = { mostrardialogoborrar = false }, // onDismissRequest para cerrar el diálogo mostrardialogo = false
            title = { Text("Confirmar eliminación") }, // Título del diálogo
            text = { Text("¿Seguro que quieres borrar la tarea '$tareaborrar'?") }, // Pregunta del diálogo
            confirmButton = {
                Button(onClick = {
                    // --- ¡¡¡ SCOPE.LAUNCH !!! ---
                    scope.launch {
                        // Aquí ocurre el borrado!!!
                        dao.borrarTarea(tareaborrar!!) // Aquí borramos de verdad
                        listaTareas.remove(tareaborrar) // Aquí la borramos de la lista
                        mostrardialogoborrar = false // false para dejar de mostrar el dialogo
                        tareaborrar = null // tareaborrar lo vaciamos
                    }
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
                "Bienvenido, $globalNombre ($globalAlias)",
                color = if(modOscuro) Color.White else Color.Black,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
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

        // --- Barra de Búsqueda ---
        OutlinedTextField(
            value = textobusqueda,
            onValueChange = { textobusqueda = it }, // Filtra automáticamente al escribir
            label = { Text("Buscar tarea...") }, // Contenido del TextField
            leadingIcon = { // Icono de búsqueda
                Icon(Icons.Filled.Search,
                    contentDescription = null)
            },
            singleLine = true, // Permite una sola línea
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Añadir tarea
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)) {
            // Campo de texto para añadir tarea
            OutlinedTextField(
                value = nuevaTareaTexto,
                onValueChange = { nuevaTareaTexto = it },
                label = {
                    Text("Nueva tarea")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Campo Fecha (Read Only) con botón calendario
                OutlinedTextField(
                    value = nuevaTareaFecha,
                    onValueChange = {},
                    label = { Text("Fecha") },
                    readOnly = true,
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        IconButton(onClick = {
                            datePicker.show()
                        }) {
                            Icon(
                                Icons.Filled.CalendarToday,
                                contentDescription = "Seleccionar fecha"
                            )
                        }
                    }
                )

                // Botón Añadir
                Button(
                    onClick = {
                        if (nuevaTareaTexto.isNotBlank() && nuevaTareaFecha.isNotBlank()) {
                            scope.launch {
                                // Guardar en ROOM
                                dao.insertarTarea(Tarea(tarea = nuevaTareaTexto, fecha = nuevaTareaFecha))
                                //cargarTareas() // Actualizar lista
                                nuevaTareaTexto = ""
                                nuevaTareaFecha = ""
                            }
                        } else {
                            // Toast.makeText(context, "Rellena texto y fecha", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) { Text("Añadir") }
            }
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
                        it.contains(textobusqueda, ignoreCase = true)
                    }
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