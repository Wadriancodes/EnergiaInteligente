package com.example.energiainteligente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.energiainteligente.database.AppDatabase
import com.example.energiainteligente.ui.screens.CadastroScreen
import com.example.energiainteligente.ui.screens.CalculadoraScreen
import com.example.energiainteligente.ui.screens.HomeScreen
import com.example.energiainteligente.ui.screens.LoginScreen
import com.example.energiainteligente.ui.theme.EnergiaInteligenteTheme
import kotlin.collections.emptyList

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val navController = rememberNavController()

    // Inicializando o Banco de Dados e Repositório do Room
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { AparelhoRepository(database.aparelhoDao()) }
    val coroutineScope = rememberCoroutineScope()

    // Observa o Flow do Banco de Dados em tempo real (se o banco mudar, a tela atualiza automaticamente)
    val listaAparelhos by repository.listarMeusAparelhos().collectAsState(initial = emptyList())

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // ... (Telas Login e Cadastro inalteradas)
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                onNavigateToCadastro = { navController.navigate(Screen.Cadastro.route) }
            )
        }
        composable(Screen.Cadastro.route) {
            CadastroScreen(
                onCadastroSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                aparelhos = listaAparelhos,
                onAdicionarAparelhoClick = { navController.navigate(Screen.calculadora.route) }
            )
        }

        composable(Screen.calculadora.route) {
            CalculadoraScreen(
                onAparelhoAdicionado = { novoAparelho ->
                    coroutineScope.launch {
                        // Salva permanentemente no banco SQLite (Room)
                        repository.salvarAparelho(novoAparelho)
                        navController.popBackStack()
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}