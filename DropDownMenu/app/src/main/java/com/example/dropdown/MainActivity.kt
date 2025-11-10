package com.example.dropdown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dropdown.ui.theme.DropdownTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DropdownTheme {
                dropDownAlumnos()
            }
        }
    }
}

@Composable
fun dropDownAlumnos(){
    var expanded by remember { mutableStateOf(false) }

    val alumnos = remember { listOf(
        Alumno("Jara Edith Maria", "40551402", true),
        Alumno("Mohammed Bachiri Jabbouri", "40551402", false),
        Alumno("Elida Maria Diez", "40551402", true),
        Alumno("Jose Juan Alberto", "40551402", true),
        Alumno("Carlos Gonzalez Maestro", "40551402", false),
        Alumno("Juan Jose Gomez", "40551402", true),
        Alumno("Maria Eduarda Dudinha", "40551402", false),
        Alumno("Alba García Gomez", "40551402", false),
        Alumno("Leyre Simon Diez", "40551402", true),
        Alumno("Carla Alberto José", "40551402", true),
    ) }

    var seleccionado by remember { mutableStateOf(alumnos[0]) }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Gray)
            .padding(top = 80.dp, start = 10.dp, end = 10.dp)

    ) {
        Text(
            text = "Seleccionado: "+seleccionado.nombre + "(" +seleccionado.dni+")",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 10.dp, start = 15.dp)
        )
        //Texto clicable
        Box(
            modifier = Modifier.fillMaxWidth()
                .size(40.dp)
                .padding(end = 10.dp, start = 10.dp)
                .background(Color.White, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = seleccionado.nombre,
                modifier = Modifier.background(Color.White)
                    .padding(start = 10.dp)
                    .clickable{expanded=true}
            )
            DropdownMenu(

                expanded = expanded,
                onDismissRequest = {expanded = false},
                modifier = Modifier.background(Color.White)
            ) {
                alumnos.forEach { alumno -> DropdownMenuItem(
                    onClick = {
                        seleccionado = alumno
                        expanded = false
                    },
                    text = {
                        Row (verticalAlignment = Alignment.CenterVertically){
                            Box(
                                modifier = Modifier.size(12.dp)
                                    .clip(CircleShape)
                                    .background(if (alumno.activo) Color.Green else Color.Red)
                            )
                            Spacer(Modifier.width(6.dp))
                            Column() {
                                Text(alumno.nombre, fontWeight = FontWeight.Bold)
                                Text(alumno.dni, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                ) }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DropdownTheme {
        dropDownAlumnos()
    }
}