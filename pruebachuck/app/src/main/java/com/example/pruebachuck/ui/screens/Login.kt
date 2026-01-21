package com.example.pruebachuck.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.pruebachuck.R
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Login(onIrAlPrincipal: () -> Unit, onIrAlRegistro: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            // Quiero una caja con shape y meter dentro el login
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x741800FD),
                contentColor = Color.White
            ),
            // quiero centrar el contenido del contenedor
        ){

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                /*
                Image(
                    painter = painterResource(id = R.drawable.pikachu),
                    contentDescription = "LogoApp",
                    modifier = Modifier.size(200.dp)
                        .padding(top = 50.dp)
                )
                */

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
                    label = { Text("E-mail") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña")},
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        cursorColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        val auth = FirebaseAuth.getInstance()
                        if(email.isBlank() || password.isBlank()){
                            Toast.makeText(context, "Email y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Login correcto", Toast.LENGTH_SHORT).show()
                                onIrAlPrincipal()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Login incorrecto", Toast.LENGTH_SHORT).show()
                            }
                    }
                ) {
                    Text(
                        text = "Iniciar sesión",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                TextButton(
                    onClick = { onIrAlRegistro() }
                ) {
                    Text(text = "Registrarse")
                }
            }

        }
    }
}




// preview
@Preview
@Composable
fun LoginPreview() {
    Login(onIrAlPrincipal = { /*TODO*/ }, onIrAlRegistro = { /*TODO*/ })
}