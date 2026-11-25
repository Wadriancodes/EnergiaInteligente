package com.example.energiainteligente.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EstadoDialog(
    onEstadoSelecionado: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    var estadoSelecionado by remember { mutableStateOf("PA") } // Pará como padrão fallback

    val ufs = listOf(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", 
        "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", 
        "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    )

    AlertDialog(
        onDismissRequest = { /* Opcional: Impedir fechamento sem escolha, mas o Material3 lida com clique fora. Vamos apenas manter o diálogo aberto */ },
        title = { Text("Selecione seu Estado") },
        text = {
            Column {
                Text("Não conseguimos obter sua localização. Por favor, selecione seu estado (UF) para calcularmos a tarifa de energia correta para a sua região.")
                Spacer(modifier = Modifier.height(16.dp))
                
                Box {
                    OutlinedButton(onClick = { expandido = true }) {
                        Text("Estado selecionado: $estadoSelecionado")
                    }
                    DropdownMenu(
                        expanded = expandido, 
                        onDismissRequest = { expandido = false }
                    ) {
                        ufs.forEach { uf ->
                            DropdownMenuItem(
                                text = { Text(uf) }, 
                                onClick = {
                                    estadoSelecionado = uf
                                    expandido = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onEstadoSelecionado(estadoSelecionado) }) {
                Text("Confirmar")
            }
        }
    )
}
