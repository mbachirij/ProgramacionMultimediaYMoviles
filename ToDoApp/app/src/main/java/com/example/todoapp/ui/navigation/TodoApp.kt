package com.example.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.home.Pantalla2
import com.example.todoapp.ui.login.Login
import com.example.todoapp.ui.register.Registro

var globalEmail = ""
var globalNombre = ""
var globalAlias = ""

@Composable
    fun TodoApp(){
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "login"
        ){
            composable("login") {
                Login(
                    onIrPantalla2 = { navController.navigate("pantalla2") },
                    onIrRegistro = { navController.navigate("registro") }
                )
            }
            composable("pantalla2") {
                Pantalla2(onIrLogin = { navController.navigate("login") })
            }
            composable("registro") {
                Registro(onVolverLogin = { navController.popBackStack() })
            }
        }
    }

