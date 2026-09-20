package com.example.energiainteligente

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Cadastro : Screen("cadastro")
    object Home : Screen("home")
    object calculadora : Screen("calculadora")
}