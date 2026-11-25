package com.example.energiainteligente.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energiainteligente.CustoDetalhado
import java.util.Locale

@Composable
fun DetalhesCustoDialog(
    custo: CustoDetalhado,
    onDismiss: () -> Unit
) {
    val ptBr = Locale("pt", "BR")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = "Detalhes: ${custo.titulo}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Consumo: ${String.format(ptBr, "%.1f", custo.consumoKWh)} kWh",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Tarifa Base: R$ ${String.format(ptBr, "%.5f", custo.tarifaBaseKWh)} / kWh",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Bandeira (Adicional): R$ ${String.format(ptBr, "%.5f", custo.adicionalBandeiraKWh)} / kWh",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Subtotal (Sem imposto): R$ ${String.format(ptBr, "%.2f", custo.precoSemImposto)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                val aliquotaPorcentagem = custo.aliquotaICMS * 100
                Text(
                    text = "ICMS (${String.format(ptBr, "%.1f", aliquotaPorcentagem)}%): R$ ${String.format(ptBr, "%.2f", custo.valorICMS)}",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Total Final: R$ ${String.format(ptBr, "%.2f", custo.valorTotal)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}
