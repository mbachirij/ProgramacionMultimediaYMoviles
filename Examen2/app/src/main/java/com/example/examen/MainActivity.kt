package com.example.examen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // COMPLETAR AQUÍ:
        enableEdgeToEdge()
        setContent {
            HealthyTheme {
                HealthyApp()
            }
        }
    }
}

/* ---------- TEMA PERSONALIZADO (Material Design 3) ---------- */
@Composable
fun HealthyTheme(content: @Composable () -> Unit) {
    val customTypography = Typography(
        titleLarge = MaterialTheme.typography.titleLarge.copy(letterSpacing = 0.5.sp),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(lineHeight = 20.sp)
    )

    val customShapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(24.dp)
    )

    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = customTypography,
        shapes = customShapes,
        content = content
    )
}

@Composable
fun HealthyApp() {
    // COMPLETAR AQUÍ: 
    var navController = rememberNavController()

    // COMPLETAR AQUÍ: Definir NavHost con dos pantallas:
    NavHost(navController = navController, startDestination = "list"){
        composable("list") {
            // Aquí se muestra la Pantalla Principal
            ActivityListScreen(onNavigateSettings = { navController.navigate("settings") })

        }
        composable("settings") {
            // Aquí se muestra la Pantalla de Configuracion
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
    // - "list" → ActivityListScreen
    // - "settings" → SettingsScreen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityListScreen(onNavigateSettings: () -> Unit) {
    val context = LocalContext.current
    // COMPLETAR AQUÍ: Estado para texto de búsqueda
    var textoBusqueda by remember { mutableStateOf("") }
    // COMPLETAR AQUÍ: Lista inicial de actividades saludables
    val actividades = remember { mutableListOf("Correr 1 hora", "Dormir 8 horas", "Beber agua 5L", "Ir al GYM 1,5 horas", "Meditar 1 hora") }
    // COMPLETAR AQUÍ: Variable para mostrar el diálogo (String?)
    var actividadDialogo by remember { mutableStateOf("") }

    var actividadSeleccionada by remember { mutableStateOf<String?>(null) }

    var pasosSimulados by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true){
            pasosSimulados = Random.nextInt(0, 15000)
            delay(2000)
        }
    }

    val snackbarHost = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
        TopAppBar(
            title = {
                Text("HealthyTracker", style = MaterialTheme.typography.titleLarge)
            },
            actions = {
                IconButton(onClick = onNavigateSettings) {
                    Icon(
                        Icons.Filled.Settings,
                        "settings" )
                }
            }
        )
    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val nuevaActividad = "Actividad ${Random.nextInt(100)}"
                    actividades.add(nuevaActividad)
                    Toast.makeText(context, "Añadida: $nuevaActividad", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir")
            }
        }
            // COMPLETAR AQUÍ: TopAppBar con título y botón de configuración
            // Usa MaterialTheme.typography.titleLarge para el título

            // COMPLETAR AQUÍ: FAB que añade una actividad aleatoria y lanza notificación
        
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // 🔍 COMPLETAR AQUÍ: Barra de búsqueda (OutlinedTextField)
            // Usa shape = MaterialTheme.shapes.medium
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar actividad") },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(12.dp))
            // 📋 COMPLETAR AQUÍ: Lista (LazyColumn) filtrada según el texto de búsqueda
            // Cada elemento debe abrir un diálogo con más información

            val listaParaMostrar =
                if(textoBusqueda.isEmpty())
                    actividades
                else
                    actividades.filter { it.contains(textoBusqueda, ignoreCase = true) }

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaParaMostrar){ actividad ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable{ actividadSeleccionada = actividad }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text( actividad)
                    }
                    HorizontalDivider(thickness = 2.dp, color = Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            // 💬 COMPLETAR AQUÍ: Diálogo informativo (AlertDialog)
            if(actividadSeleccionada != null){
                AlertDialog(
                    onDismissRequest = { actividadSeleccionada = null },
                    title = { Text("Actividad seleccionada") },
                    text = { Text("Has seleccionado $actividadSeleccionada") },
                    confirmButton = {
                        TextButton(onClick = { actividadSeleccionada = null }) {
                            Text("Cerrar")
                        }
                    }
                )
            }
            // ⚙️ COMPLETAR AQUÍ: Mostrar un valor simulado de un sensor (por ejemplo, pasos)
            // Aplica MaterialTheme.typography.bodyLarge
            Text(
                text = "Pasos de hoy: $pasosSimulados",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(false) }

    val snackbarHost = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
        TopAppBar(
            title = {
                Text("Preferencias", style = MaterialTheme.typography.titleLarge)
            },
            actions = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        "Volver" )
                }
            }
        )
    },
        
            // COMPLETAR AQUÍ: TopAppBar con botón para volver atrás
        
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Preferencias de notificación",
                style = MaterialTheme.typography.titleMedium
            )

            // COMPLETAR AQUÍ: Switch para activar/desactivar notificaciones
            // Al activarlo, debe llamar a showNotification()
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { puedo ->
                    notificationsEnabled = puedo
                }
            )
        }
    }
}

// 🔔 Función auxiliar para mostrar notificaciones
fun showNotification(context: Context, message: String) {
    val channelId = "healthy_channel"
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // COMPLETAR AQUÍ: Crear canal de notificación

    // COMPLETAR AQUÍ: Construir y mostrar la notificación con NotificationCompat.Builder

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
        SettingsScreen(onBack = { })
}