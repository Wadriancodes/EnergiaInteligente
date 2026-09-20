package com.example.energiainteligente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.energiainteligente.ui.screens.CadastroScreen
import com.example.energiainteligente.ui.screens.CalculadoraScreen
import com.example.energiainteligente.ui.screens.HomeScreen
import com.example.energiainteligente.ui.screens.LoginScreen
import com.example.energiainteligente.ui.theme.EnergiaInteligenteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnergiaInteligenteTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

// Lista pré-populada com os 3 eletrodomésticos do protótipo Figma
    val listaAparelhos = remember {
        mutableStateListOf(
            Aparelho(nome = "Geladeira", potenciaWatts = 127.0, horasPorDia = 24.0),
            Aparelho(nome = "Ar-condicionado", potenciaWatts = 900.0, horasPorDia = 8.0),
            Aparelho(nome = "PC Gamer", potenciaWatts = 250.0, horasPorDia = 6.0)
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 1. Tela de Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToCadastro = {
                    navController.navigate(Screen.Cadastro.route)
                }
            )
        }

        // 2. Tela de Cadastro
        composable(Screen.Cadastro.route) {
            CadastroScreen(
                onCadastroSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // 3. Tela Home (Dashboard & Aparelhos)
        composable(Screen.Home.route) {
            HomeScreen(
                aparelhos = listaAparelhos,
                onAdicionarAparelhoClick = {
                    navController.navigate(Screen.calculadora.route)
                }
            )
        }

        // 4. Tela Calculadora (Novo Aparelho)
        composable(Screen.calculadora.route) {
            CalculadoraScreen(
                onAparelhoAdicionado = { novoAparelho ->
                    listaAparelhos.add(novoAparelho)
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}