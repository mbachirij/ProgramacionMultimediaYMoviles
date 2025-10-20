package com.example.myapplication3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication3.ui.theme.MyApplication3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication3Theme {
                Calculadora()
            }
        }
    }
}

@Composable
fun Calculadora() {



    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()

            .background(Color.Black)
            .padding(15.dp),


        ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            //Pantalla Arriba
            Box(
                modifier = Modifier

                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),


                ) {
                Text(
                    text = "0",
                    color = Color.White,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)


                    //falta la posicion de ese 0...
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Botones de abajo
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),

                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {

                //Fila 1
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) { Text("AC", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) { Text("±", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) { Text("%", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color(0xFFFFB747)),
                        contentAlignment = Alignment.Center
                    ) { Text("/", color = Color.White) }
                }

                //File 2

                Row(
                    modifier = Modifier

                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("7", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("8", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("9", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color(0xFFFFB747)),
                        contentAlignment = Alignment.Center
                    ) { Text("x", color = Color.White) }

                }
                //Fila 33333
                Row(
                    modifier = Modifier

                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("4", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("5", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("6", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color(0xFFFFB747)),
                        contentAlignment = Alignment.Center
                    ) { Text("-", color = Color.White) }
                }

                //Fila 4
                Row(
                    modifier = Modifier

                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("1", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("2", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("3", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color(0xFFFFB747)),
                        contentAlignment = Alignment.Center
                    ) { Text("+", color = Color.White) }
                }

                //Fila 5
                Row(
                    modifier = Modifier

                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("0", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text(".", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) { Text("<x", color = Color.White) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color(0xFFFFB747)),
                        contentAlignment = Alignment.Center
                    ) { Text("=", color = Color.White) }
                }

            }


        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplication3Theme {
        Calculadora()
    }
}