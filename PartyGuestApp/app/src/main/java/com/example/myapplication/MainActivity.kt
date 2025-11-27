package com.example.myapplication


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle

import android.util.Log

import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material.icons.filled.Delete

import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button

import androidx.compose.material3.DropdownMenu

import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.Icon

import androidx.compose.material3.IconButton

import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

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

import androidx.compose.ui.text.font.FontStyle

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController

import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            MyApplicationTheme {

                AppNavigation()
            }
        }
    }
}


var nombreOrganizador = "";

var tipoEvento = "";


@Composable
fun AppNavigation() {
    var navController = rememberNavController()
    NavHost(navController = navController, startDestination = "portada") {

        composable("portada") {
            // Aquí se muestra la pantalla de inicio de sesión
            Portada(onIrInvitados = { navController.navigate("invitados") })
        }
        composable("invitados") {
            // Aquí se muestra la pantalla de invitados
            Invitados(onVolverPortada = { navController.navigate("portada") })
        }
    }
}


@Composable
fun Portada(onIrInvitados: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var nombreEvento by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logopartyguests),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(top = 20.dp)
        )
        Text(
            "Party Guests",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            )
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = nombreEvento,
            onValueChange = { nombreEvento = it },
            label = { Text("Nombre del Evento") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Button(
            onClick = {
                if (nombre.isNotBlank() && nombreEvento.isNotBlank()) {
                    nombreOrganizador = nombre;
                    tipoEvento = nombreEvento;
                    onIrInvitados()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Abrir lista")
        }
    }
}


@Composable
fun Invitados(onVolverPortada: () -> Unit) {
    // Lista de invitados
    val listaInvitados = remember { mutableStateListOf<String>() }
    // variable para el nuevo invitado
    var nuevoInvitado by remember { mutableStateOf("") }
    var textobusqueda by remember { mutableStateOf("") }
    // variable para salir de la portada
    var salirInvitados by remember { mutableStateOf(false) }
    // variable para abrir preferencias
    var preferencias by remember { mutableStateOf(false) }
    var mostrarPreferencias by remember { mutableStateOf(false) }

    // Variables para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var mostrarSnackbar by remember { mutableStateOf(false) }

    var modoOscuro by remember { mutableStateOf(false) }
    val context = LocalContext.current

    //Relacionado al sensor de luz.
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager
    val sensorLuz = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT)

    //Con ayuda del sensor de luz hacemos que si hay poca luz la aplicación se pone en modo oscuro.
    DisposableEffect(Unit) {
        val listener = object : android.hardware.SensorEventListener {
            override fun onSensorChanged(event: android.hardware.SensorEvent?) {
                //Pilla la cantidad de luz a tiempo real y la guarda aquí.
                val cantidadLuz = event?.values?.get(0) ?: 0f
                //La aplicación se pone en modo oscuro si hay poca luz
                modoOscuro = cantidadLuz < 40f
            }

            override fun onAccuracyChanged(sensor: android.hardware.Sensor?, accuracy: Int) {}
        }

        //Registramos el listener
        sensorManager.registerListener(listener, sensorLuz, android.hardware.SensorManager.SENSOR_DELAY_UI)

        //Cerramos el listener
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // Mover el Scaffold aquí afuera para que contenga toda la UI
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(if(modoOscuro) Color.LightGray else Color.White)
                .fillMaxSize()
        ) {
            // Aquí va todo el contenido que ya tenías...
            if (salirInvitados) {
                AlertDialog(
                    onDismissRequest = { salirInvitados = false },
                    title = { Text("Volver a la portada") },
                    text = { Text("¿Está seguro que desea volver a la portada?") },
                    confirmButton = {
                        Button(onClick = { onVolverPortada() }) {
                            Text("Segurísimo")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { salirInvitados = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp)
            ) {
                // Texto de bienvenida
                Text(
                    "Bienvenido $nombreOrganizador a $tipoEvento",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                // Botton para volver a la portada
                IconButton(onClick = { salirInvitados = true }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                // Boton de preferencias
                IconButton(onClick = { preferencias = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Preferencias",
                        tint = Color.Black
                    )
                    DropdownMenu(
                        expanded = preferencias,
                        onDismissRequest = { preferencias = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Preferencias") },
                            onClick = {
                                preferencias = false // Cerrar cuando le de a click
                                mostrarPreferencias = true // Abrimos el diálogo de preferencias
                            }
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 2.dp, color = Color.Black)
            Text(
                "Para apuntarse este evento tiene que pulsar 'Apuntarme'",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
            // Campo de texto para buscar invitados
            OutlinedTextField(
                value = textobusqueda,
                onValueChange = { textobusqueda = it },
                label = { Text("Buscar invitado...") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Buscar"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, start = 10.dp)
            )
            Text(
                "Lista de asistentes al evento:",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
            // LazyColumn para mostrar la lista de invitados y filtrarlos por nombre
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val listaParaMostrar =
                    if (textobusqueda.isEmpty())
                        listaInvitados
                    else
                        listaInvitados.filter { it.contains(textobusqueda, ignoreCase = true) }

                // Recorremos la listaParaMostrar para mostrar cada invitado
                items(listaParaMostrar) { invitado ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            invitado,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            listaInvitados.remove(invitado)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    HorizontalDivider(thickness = 2.dp, color = Color.Black)
                }
            }
            Button(
                onClick = {
                    // Añadir el organizador a la lista
                    listaInvitados.add(nombreOrganizador)
                    // Mostrar el Snackbar
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Invitado añadido correctamente",
                            actionLabel = "Deshacer"
                        )
                        // Si el usuario pulsa "Deshacer", eliminar el invitado
                        if (result == SnackbarResult.ActionPerformed) {
                            listaInvitados.remove(nombreOrganizador)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text("Apuntarme")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Invitados(onVolverPortada = {})
    }
}