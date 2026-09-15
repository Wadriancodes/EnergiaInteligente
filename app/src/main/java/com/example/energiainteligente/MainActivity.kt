package com.example.energiainteligente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.energiainteligente.ui.theme.EnergiaInteligenteTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnergiaInteligenteTheme {
                Home()
            }
        }
    }
}

@Composable
fun Home() {
    var nomeAparelho by remember { mutableStateOf("") }
    var potencia by remember { mutableStateOf("") }
    var horasPorDia by remember { mutableStateOf("") }
    var resultadoTexto by remember { mutableStateOf("") }

    val precoKWh = 0.97830

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00B914)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Energia Inteligente",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = nomeAparelho,
                onValueChange = { novoTexto -> nomeAparelho = novoTexto },
                label = { Text("Nome do aparelho") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = potencia,
                onValueChange = { novoTexto -> potencia = novoTexto },
                label = { Text("Potência (W)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = horasPorDia,
                onValueChange = { novoTexto -> horasPorDia = novoTexto },
                label = { Text("Horas por dia") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Preço do KWh no Pará: R$ 0,97830",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val p = potencia.toDoubleOrNull()
                    val h = horasPorDia.toDoubleOrNull()

                    if (p != null && h != null) {
                        // Chamando as funções do arquivo CalculoEnergia.kt
                        val consumoKWhMensal = calcularConsumoMensal(p, h)
                        val custoMensal = calcularCustoMensal(p, h, precoKWh)
                        
                        resultadoTexto = String.format(
                            Locale.getDefault(),
                            "O aparelho '%s' consumirá %.2f kWh no mês.\nCusto Estimado: R$ %.2f",
                            nomeAparelho.ifEmpty { "Aparelho" },
                            consumoKWhMensal,
                            custoMensal
                        )
                    } else {
                        resultadoTexto = "Por favor, insira valores válidos para potência e horas."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Calcular")
            }

            if (resultadoTexto.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = resultadoTexto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (resultadoTexto.startsWith("Por favor")) Color.Red else Color.Black
                )
            }
        }
    }
}