package com.example.todoapp.ui.home

import android.Manifest
import android.R
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import com.example.todoapp.Tarea
import com.example.todoapp.data.repository.TareaRepositoryFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import com.google.firebase.auth.FirebaseAuth


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
    val email = FirebaseAuth.getInstance().currentUser?.email ?: ""

    var nuevaTareaTexto by remember { mutableStateOf("") }
    var nuevaTareaFecha by remember { mutableStateOf("") }

    var preferencias by remember { mutableStateOf(false) }
    var mostrarpreferencias by remember { mutableStateOf(false) }
    var colorText by remember { mutableStateOf(Color.White) }
    var modOscuro by remember { mutableStateOf(false) }

    var textobusqueda by remember { mutableStateOf("") }
    var mostrardialogoborrar by remember { mutableStateOf(false) }
    var tareaborrar by remember { mutableStateOf<Tarea?>(null) }

    val repo = remember { TareaRepositoryFirestore() }
    val listaTareas = remember { mutableStateListOf<Tarea>() }

    var pokemon: String = remember { mutableStateOf("").toString() }

    // Cargar tareas desde Firestore
    LaunchedEffect(Unit) {
        listaTareas.clear()
        listaTareas.addAll(repo.obtenerTareas())
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
        delay(18000L) // 3 minutos

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
            .setSmallIcon(R.drawable.ic_dialog_info)
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
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
                            repo.borrarTarea(tareaParaBorrar.id)
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
                "Bienvenido, $email",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
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
                                val tareas = repo.obtenerTareas()
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
            colors = TextFieldDefaults.colors(
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
                colors = TextFieldDefaults.colors(
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
                                Icons.Filled.DateRange,
                                contentDescription = "Seleccionar fecha"
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x41000000),
                        contentColor = Color.White
                    ),
                    onClick = {
                        if (nuevaTareaTexto.isNotBlank() && nuevaTareaFecha.isNotBlank()) {

                            scope.launch {
                                // cojo la última palabra
                                val ultimaPalabra = nuevaTareaTexto.split(" ").last().trim()


                                // Variables vacias por si no encuentro nada
                                var nombre: String? = null
                                var imagen: String? = null
                                var tipo: String? = null
                                var stats: String? = null



                                // compruebo que ultima palabra no esté vacía
                                if (ultimaPalabra.isNotEmpty()) {
                                    try {
                                        val respuesta = com.example.todoapp.data.network.RetrofitClient.api.getPokemonByName(ultimaPalabra)

                                        // si estoy en este paso es que ya he enconttrado el pokemon
                                        nombre = respuesta.name
                                        imagen = respuesta.sprites.front_default
                                        tipo = respuesta.types.firstOrNull()?.type?.name

                                        // esto es para poner los stats bonitos
                                        stats = respuesta.stats.joinToString(", ") { stat ->
                                            "${stat.stat.name}: ${stat.base_stat}"
                                        }

                                        Toast.makeText(context, "¡Pokémon $nombre detectado!", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        // Si falla (no es un pokemon), seguimos normal sin datos extra
                                        println("No es un pokemon o error de red: ${e.message}")
                                    }
                                }

                                // 3. Crear el objeto Tarea
                                val nuevaTarea = Tarea(
                                    tarea = nuevaTareaTexto,
                                    fecha = nuevaTareaFecha,
                                    pokemonNombre = nombre,
                                    pokemonImagen = imagen,
                                    pokemonTipo = tipo,
                                    pokemonStats = stats
                                )

                                // Guardar en Firestore
                                repo.insertarTarea(nuevaTarea) // <--- Nota: Actualiza tu repositorio para aceptar el objeto Tarea

                                // Limpiar los campos
                                listaTareas.clear()
                                listaTareas.addAll(repo.obtenerTareas())
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
                        // muestro la imagen del pokemon si la hay
                        if(tarea.pokemonImagen != null){
                            // coil es una librería para cargar imágenes
                            coil.compose.AsyncImage(
                                model = tarea.pokemonImagen,
                                contentDescription = "Pokemon",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 8.dp)
                            )
                        }

                        // Columna con los textos de la tarea
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

                            // si el pokemon tiene más datos, los muestro
                            if(tarea.pokemonStats != null){
                                Text(
                                    text = "Tipo: "+tarea.pokemonStats,
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Stats: "+tarea.pokemonStats,
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                            }

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