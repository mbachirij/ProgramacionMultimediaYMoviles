package com.example.loginfirestore



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {

                val navController = rememberNavController()
                val auth = FirebaseAuth.getInstance()

                //  AUTO LOGIN
                val startDestination =
                    if (auth.currentUser != null)
                        "welcome/${auth.currentUser?.email}"
                    else
                        "login"

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {

                    // ---------------------------------------------------
                    // LOGIN
                    // ---------------------------------------------------
                    composable("login") {
                        LoginAndWelcomeScreen.LoginScreen(
                            onLoginSuccess = { username ->
                                navController.navigate("welcome/$username") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                navController.navigate("register")
                            }
                        )
                    }

                    // ---------------------------------------------------
                    // REGISTRO
                    // ---------------------------------------------------
                    composable("register") {
                        LoginAndWelcomeScreen.RegisterScreen(
                            onRegisterSuccess = { username ->
                                navController.navigate("welcome/$username") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onBackToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // ---------------------------------------------------
                    // WELCOME + LOGOUT
                    // ---------------------------------------------------
                    composable(
                        route = "welcome/{username}",
                        arguments = listOf(navArgument("username") { type = NavType.StringType })
                    ) { backStackEntry ->

                        val username = backStackEntry.arguments?.getString("username") ?: ""

                        LoginAndWelcomeScreen.WelcomeScreen(
                            username = username,
                            onLogout = {
                                auth.signOut()
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
