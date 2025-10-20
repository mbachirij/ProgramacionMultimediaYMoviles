package com.example.navhost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navhost.ui.theme.NavHostTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavHostTheme {
                AppNav()
            }
        }
    }
}



@Composable
fun AppNav(){
    var navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "Pantalla1"
    ){
        composable("pantalla1") {
            Pantalla1(onIrPantalla2 = {navController.navigate("pantalla2")}

            )
        }
        composable("pantalla2") {
            Pantalla2(onBack = {navController.popBackStack()}

            )
        }
    }

}


@Composable
fun Pantalla1(onIrPantalla2: () -> Unit ){

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center


    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Estoy en la pantalla 1")
            Spacer(Modifier.height(20.dp))

            Button(onClick = onIrPantalla2) {
                Text(text = "Ir a la pantalla 2")
            }

        }
    }

}

@Composable
fun Pantalla2(onBack: () -> Unit){

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center


    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Estoy en la pantalla 2")
            Spacer(Modifier.height(20.dp))

            Button(onClick = onBack ) {
                Text(text = "Ir a la pantalla 1")
            }

        }
    }

}


