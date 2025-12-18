package com.example.todoapp

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.todoapp.ui.theme.TodoappTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
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
var globalEmail = ""
var globalContrasena = ""

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
            Pantalla2(onIrLogin = { navController.navigate("login") })
        }
        composable("registro") {
            Registro(onVolverLogin = { navController.popBackStack() })
        }
    }
}


// Pantalla 1: LOGIN
@Composable
fun Login(onIrPantalla2: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance();
    val context = LocalContext.current

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
            text = "Bienvenido, Ingrese su email y contraseña",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it},
            label = { Text("E-Mail") },
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
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña")},
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
            onClick = {
                if (email.isNotBlank() && contrasena.isNotBlank()) {
                    auth.signInWithEmailAndPassword(email, contrasena)
                        .addOnSuccessListener {
                            globalEmail = email
                            onIrPantalla2()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Usuario no registrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        ) {
            Text("Iniciar sesión")
        }

        TextButton(onClick = { onIrRegistro() }) {
            Text("¿No tienes cuenta? Regístrate", color = Color.White)
        }

    }

}
fun exportarTareas(context: Context, tareas: List<Tarea>) {
    try {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val file = File(downloadsDir, "tareas.txt")

        val contenido = buildString {
            tareas.forEach {
                append("Tarea: ${it.tarea}\n")
                append("Fecha: ${it.fecha}\n")
                append("-------------------\n")
            }
        }

        file.writeText(contenido)

        Toast.makeText(
            context,
            "Tareas exportadas en Descargas/tareas.txt",
            Toast.LENGTH_LONG
        ).show()

    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error al exportar tareas",
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
fun Pantalla2(onIrLogin: () -> Unit) {
    val context = LocalContext.current
    val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val proximitySensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    }

    var objetoCerca by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


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

    DisposableEffect(Unit) {

        if (proximitySensor == null) {
            onDispose { }
        } else {

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    objetoCerca = event.values[0] < proximitySensor.maximumRange
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    LaunchedEffect(listaTareas.size) {
        delay(1800L) // 3 minutos

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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { }

        LaunchedEffect(Unit) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
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
            .background(
                if (objetoCerca) Color(0xDAFF0000)
                else if (modOscuro) Color(0xE6000000)
                else Color(0xD00E0E0E)
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 70.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                "Bienvenido, $globalEmail",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0x41000000),
                    contentColor = Color.Black
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
                            scope.launch {
                                val tareas = dao.obtenerTareas()
                                exportarTareas(context, tareas)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Salir", color = Color.White) },
                        onClick = {
                            preferencias = false
                            onIrLogin()
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
                        containerColor = Color(0x41000000),
                        contentColor = Color.White
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

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (modOscuro)
                            Color(0xFF1E1E1E)
                        else
                            Color(0xFF2A2A2A)
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = tarea.tarea,
                                color = colorText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tarea.fecha,
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }

                        IconButton(
                            onClick = {
                                tareaborrar = tarea
                                mostrardialogoborrar = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoappTheme {
        Pantalla2(onIrLogin = {})
    }
}
