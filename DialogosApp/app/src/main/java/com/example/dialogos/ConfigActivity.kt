package com.example.dialogos

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialogos.ui.theme.DialogosTheme

class ConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DialogosTheme {
                Configuracion()
            }
        }
    }
}

@Composable
fun Configuracion(){
    //Para volver al MaiActivity:
    val context = LocalContext.current
    val intent = Intent( context, MainActivity::class.java)

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //Para volver al MainActivity
            TextButton(onClick = { context.startActivity(intent) }) {
                Text("<- Volver")
            }
            Image(
                painter = painterResource(id = R.drawable.shoppingbags),
                contentDescription = "LogoApp",
                modifier = Modifier
                    .size(25.dp)
            )
            Text(
                "Configurar pedido.",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp),
                //Para cambiar el color del botón
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Eliminar del carrito")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Método de pago")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Avisos de envío")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Alias reseñas")
            }
        }
        Box(modifier = Modifier.fillMaxWidth()
            .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp),

                ) {
                    Text("Confirmar")
                }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {  }



}

@Preview(showBackground = true)
@Composable
fun Preview() {
    DialogosTheme {
        Configuracion()
    }
}