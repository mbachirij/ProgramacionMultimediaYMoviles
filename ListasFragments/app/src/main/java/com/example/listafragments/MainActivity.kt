package com.example.listafragments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listafragments.ui.theme.ListaFragmentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaFragmentsTheme {
                ListaSimple()
            }
        }
    }
}

@Composable
fun ListaSimple(){
    val alumnos = remember { listOf(
        Alumno("Alumno1", "99999999", true),
        Alumno("Alumno2", "99999999", false),
        Alumno("Alumno3", "99999999", true),
        Alumno("Alumno4", "99999999", true),
        Alumno("Alumno5", "99999999", false),
        Alumno("Alumno6", "99999999", true),
        Alumno("Alumno7", "99999999", false),
        Alumno("Alumno8", "99999999", false),
        Alumno("Alumno9", "99999999", true),
        Alumno("Alumno10", "99999999", true),
    ) }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(top = 50.dp, start = 10.dp, end = 10.dp)
    ) {
        items(alumnos){alumno ->
            ItemAlumnoBasico(alumno)
        }
    }
}

@Composable
fun ItemAlumnoBasico(alumno: Alumno){

    Row (
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
          modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(Color.Blue),
            contentAlignment = Alignment.Center
        ){
            Icon(
                Icons.Default.Person,
                contentDescription = "avatar",
                tint = Color.Cyan
            )
        }
        Spacer(Modifier.width(10.dp))
        Column (modifier = Modifier.weight(1f)){
            Text(
                text = alumno.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = alumno.dni,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Text(
            text = if(alumno.activo) "activo" else "Inactivo",
            color = if(alumno.activo) Color.Green else Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListaFragmentsTheme {
        ListaSimple()
    }
}