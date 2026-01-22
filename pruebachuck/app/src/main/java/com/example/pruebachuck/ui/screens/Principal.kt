package com.example.pruebachuck.ui.screens

import android.app.Application
import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pruebachuck.R
import com.example.pruebachuck.ui.ChisteViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun Principal(
    onIrAlLogin: () -> Unit,
    viewModel: ChisteViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
){



    val chistes by viewModel.listaChistes.observeAsState(emptyList())



    var preferencias by remember { mutableStateOf(false) }
    var mostrarpreferencias by remember { mutableStateOf(false) }

    var nuevoChiste by remember { mutableStateOf("") }

    val context = LocalContext.current

    var nuevaFecha by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            nuevaFecha = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(Unit) {
        viewModel.sincronizarSiHayUsuario()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0562B)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Bienvenido a tu app Chuck Norris",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.padding(horizontal = 50.dp))

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFFF3E00),
                    contentColor = Color.Black
                ),
                onClick = { preferencias = true }
            ) {

                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Preferencias",
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
                        text = { Text("Salir", color = Color.White) },
                        onClick = {
                            preferencias = false
                            onIrAlLogin()
                        }
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)){

            OutlinedTextField(
                value = nuevoChiste,
                onValueChange = { nuevoChiste = it },
                label = { Text(
                    "Escribe el nombre de un pokemon. Ejemplo: Pikachu",
                    fontSize = 12.sp
                )},
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
                    value = nuevaFecha,
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
                        containerColor = Color(0xFFFF3E00),
                        contentColor = Color.White
                    ),
                    onClick = {
                        val chiste = viewModel.cargarChisteAleatorio()


                    },
                ) {
                    Text("Añadir")
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(chistes) { chiste ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.DarkGray
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            Text(
                                text = chiste.texto,
                                color = Color.White,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { viewModel.borrarChiste(chiste) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("Borrar")
                            }
                        }
                    }
                }
            }


        }

    }

}

// preview
@Preview
@Composable
fun PrincipalPreview() {
    Principal(onIrAlLogin = { /*TODO*/ })
}
