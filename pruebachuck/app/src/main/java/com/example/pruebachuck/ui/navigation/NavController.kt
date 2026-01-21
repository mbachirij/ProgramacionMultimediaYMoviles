package com.example.pruebachuck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebachuck.ui.screens.Login
import com.example.pruebachuck.ui.screens.Principal
import com.example.pruebachuck.ui.screens.Register
import java.security.Principal

@Composable
fun NavController() {
    // variable para controlar la navegación
    val navController = rememberNavController()

    // estructura de navegación
    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login"){
            Login(
                onIrAlPrincipal = { navController.navigate("principal") },
                onIrAlRegistro = { navController.navigate("register") })
        }
        composable("register"){
            Register(onIrAlLogin = { navController.navigate("login") })
        }
        composable("principal"){
            Principal(onIrAlLogin = { navController.navigate("login") })
        }
    }

}