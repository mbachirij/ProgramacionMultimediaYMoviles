package com.example.todoapp

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.todoapp.ui.theme.TodoappTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

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


// Pantalla 1: LOGIN
@Composable
fun Login(onIrPantalla2: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xF50E0E0E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoappnuevo),
            contentDescription = "LogoApp",
            modifier = Modifier.size(200.dp)
                .padding(top = 50.dp)
        )

        Text(
            text = "Bienvenido, Ingrese su nombre y alias",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it},
            label = { Text("Nombre") },
            singleLine = true,
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        OutlinedTextField(
            value = alias,
            onValueChange = { alias = it },
            label = { Text("Alias")},
            singleLine = true,
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        )
        Button(
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                if (nombre.isNotBlank() && alias.isNotBlank()){
                    globalNombre = nombre
                    globalAlias = alias
                    onIrPantalla2()
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tareas_room.db"
        )
            .allowMainThreadQueries()
            .build()
    }
    val dao = db.tareaDao()

    var nuevaTareaTexto by remember { mutableStateOf("") }
    var nuevaTareaFecha by remember { mutableStateOf("") }

    val listaTareas = remember { mutableStateListOf<Tarea>() }

    var preferencias by remember { mutableStateOf(false) }
    var mostrarpreferencias by remember { mutableStateOf(false) }
    var colorText by remember { mutableStateOf(Color.White) }
    var modOscuro by remember { mutableStateOf(false) }

    var textobusqueda by remember { mutableStateOf("") }
    var mostrardialogoborrar by remember { mutableStateOf(false) }
    var tareaborrar by remember { mutableStateOf<Tarea?>(null) }

    LaunchedEffect(true) {
        val tareas = dao.obtenerTareas()
        listaTareas.clear()
        listaTareas.addAll(tareas)
    }

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
                Button(onClick = { mostrarpreferencias = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (mostrardialogoborrar) {
        val tareaParaBorrar = tareaborrar
        if (tareaParaBorrar != null) {
            AlertDialog(
                onDismissRequest = { mostrardialogoborrar = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Seguro que quieres borrar la tarea '${tareaParaBorrar.tarea}'?") },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            dao.borrarTarea(tareaParaBorrar)
                            listaTareas.remove(tareaParaBorrar)
                            mostrardialogoborrar = false
                            tareaborrar = null
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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( if (modOscuro) Color(0xFF000000) else Color(0xF50E0E0E) ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 70.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                "Bienvenido, $globalNombre ($globalAlias)",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                onClick = { preferencias = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Opciones",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                DropdownMenu(
                    expanded = preferencias,
                    onDismissRequest = { preferencias = false },
                    modifier = Modifier.background(Color(0xF50E0E0E))
                ) {
                    DropdownMenuItem(
                        text = { Text("Preferencias", color = Color.White) },
                        onClick = {
                            preferencias = false
                            mostrarpreferencias = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Exportar tareas", color = Color.White) },
                        onClick = {
                            preferencias = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = textobusqueda,
            onValueChange = { textobusqueda = it },
            label = { Text("Buscar tarea...") },
            leadingIcon = { 
                Icon(Icons.Filled.Search,
                    contentDescription = null)
            },
            singleLine = true,
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = nuevaTareaTexto,
                onValueChange = { nuevaTareaTexto = it },
                label = { Text("Nueva tarea")},
                singleLine = true,
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = nuevaTareaFecha,
                    onValueChange = { },
                    label = { Text("Fecha") },
                    readOnly = true,
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        IconButton(onClick = { datePicker.show() }) {
                            Icon(
                                Icons.Filled.CalendarToday,
                                contentDescription = "Seleccionar fecha"
                            )
                        }
                    },
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )

                Button(
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if (nuevaTareaTexto.isNotBlank() && nuevaTareaFecha.isNotBlank()) {
                            scope.launch {
                                val nueva = Tarea(fecha = nuevaTareaFecha, tarea = nuevaTareaTexto)
                                dao.insertarTarea(nueva)
                                listaTareas.clear()
                                listaTareas.addAll(dao.obtenerTareas())
                                Toast.makeText(context, "Tarea añadida", Toast.LENGTH_SHORT).show()
                                nuevaTareaTexto = ""
                                nuevaTareaFecha = ""
                            }
                        } else {
                            Toast.makeText(context, "Rellena texto y fecha", Toast.LENGTH_SHORT).show()
                        }
                    },
                ) { Text("Añadir") }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val listaParaMostrar =
                if (textobusqueda.isEmpty()) {
                    listaTareas
                } else {
                    listaTareas.filter {
                        it.tarea.contains(textobusqueda, ignoreCase = true)
                    }
                }
            items(listaParaMostrar) { tarea ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${tarea.tarea} (${tarea.fecha})",
                        modifier = Modifier.weight(1f),
                        color = colorText,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = {
                        tareaborrar = tarea
                        mostrardialogoborrar = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
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
