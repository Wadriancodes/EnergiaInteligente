package com.example.energiainteligente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energiainteligente.Aparelho

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraScreen(
    onAparelhoAdicionado: (Aparelho) -> Unit,
    onBackClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var potencia by remember { mutableStateOf("") }
    var horasPorDia by remember { mutableStateOf("") }
    var erroTexto by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Aparelho", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Aparelho (ex: Ar Condicionado)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = potencia,
                onValueChange = { potencia = it },
                label = { Text("Potência (Watts)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = horasPorDia,
                onValueChange = { horasPorDia = it },
                label = { Text("Uso diário (Horas)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (erroTexto.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = erroTexto, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val p = potencia.toDoubleOrNull()
                    val h = horasPorDia.toDoubleOrNull()

                    if (nome.isNotEmpty() && p != null && h != null) {
                        val novoAparelho = Aparelho(
                            nome = nome,
                            potenciaWatts = p,
                            horasPorDia = h
                        )
                        onAparelhoAdicionado(novoAparelho)
                    } else {
                        erroTexto = "Por favor, preencha todos os campos corretamente."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Salvar Aparelho", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}