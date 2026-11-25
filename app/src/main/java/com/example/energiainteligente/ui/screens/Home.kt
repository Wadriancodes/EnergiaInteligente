package com.example.energiainteligente.ui.screens

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energiainteligente.AneelRepository
import com.example.energiainteligente.Aparelho
import com.example.energiainteligente.BandeiraTarifaria
import com.example.energiainteligente.CustoDetalhado
import com.example.energiainteligente.R
import com.example.energiainteligente.TarifaDistribuidora
import com.example.energiainteligente.network.LocationService
import com.example.energiainteligente.ui.components.DetalhesCustoDialog
import com.example.energiainteligente.ui.components.EstadoDialog
import com.example.energiainteligente.ui.components.UserPerfilDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    aparelhos: List<Aparelho>,
    onAdicionarAparelhoClick: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { AneelRepository() }
    val locationService = remember { LocationService(context) }
    val scope = rememberCoroutineScope()

    var showEstadoDialog by remember { mutableStateOf(false) }
    var detalhesSelecionados by remember { mutableStateOf<CustoDetalhado?>(null) }
    var showPerfilDialog by remember { mutableStateOf(false) }

    // Estados carregados assincronamente da API REST ANEEL
    var bandeira by remember { mutableStateOf(BandeiraTarifaria.verde()) }
    var tarifa by remember { mutableStateOf(TarifaDistribuidora("PA", "Equatorial Pará", 0.97830)) }

    // Estado da Permissão de Localização
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { isGranted ->
        if (isGranted) {
            scope.launch {
                val uf = locationService.obterUfDoUsuario()
                if (uf != null) {
                    tarifa = repository.obterTarifaPorUF(uf)
                } else {
                    showEstadoDialog = true
                }
            }
        } else {
            showEstadoDialog = true
        }
    }

    // Requisição inicial em background
    LaunchedEffect(Unit) {
        bandeira = repository.obterBandeiraVigente()
        
        if (locationPermissionState.status.isGranted) {
            val uf = locationService.obterUfDoUsuario()
            if (uf != null) tarifa = repository.obterTarifaPorUF(uf)
            else showEstadoDialog = true
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    if (showEstadoDialog) {
        EstadoDialog(
            onEstadoSelecionado = { ufSelecionado ->
                showEstadoDialog = false
                scope.launch {
                    tarifa = repository.obterTarifaPorUF(ufSelecionado)
                }
            }
        )
    }

    detalhesSelecionados?.let { detalhes ->
        DetalhesCustoDialog(
            custo = detalhes,
            onDismiss = { detalhesSelecionados = null }
        )
    }

    if (showPerfilDialog) {
        UserPerfilDialog(
            uf = tarifa.uf,
            distribuidora = tarifa.distribuidora,
            onDismiss = { showPerfilDialog = false },
            onLogoutClick = onLogout
        )
    }

    val consumoTotalKWh = aparelhos.sumOf { it.consumoMensalKWh }
    val custoTotalEst = aparelhos.sumOf {
        it.calcularCustoMensal(tarifa, bandeira.adicionalPorKWh, consumoTotalKWh)
    }

    val ptBr = Locale("pt", "BR")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_energia),
                    contentDescription = "Logo Energia Inteligente",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Energia Inteligente",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f) // Ocupa o espaço do meio empurrando o ícone pro canto
                )

                // Botão de Perfil (Direita)
                IconButton(
                    onClick = { showPerfilDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdicionarAparelhoClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Aparelho",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Cabeçalho de Custo Total
            Text(
                text = "Custo total do mês",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Custo Total do Mês em Grande Destaque
                Text(
                    text = String.format(ptBr, "R$ %.2f", custoTotalEst),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Badge da Bandeira Vigente (Cores dinâmicas da bandeira)
                Surface(
                    color = bandeira.corHex.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = bandeira.corHex,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = bandeira.nome,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = bandeira.corHex
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Card de Consumo Estimado (Clicável para ver detalhes totais)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val aliquota = tarifa.calcularAliquotaICMS(consumoTotalKWh)
                        val precoSemImposto = (tarifa.tarifaBaseKWh + bandeira.adicionalPorKWh) * consumoTotalKWh
                        val valorImposto = precoSemImposto * aliquota
                        detalhesSelecionados = CustoDetalhado(
                            titulo = "Custo Total da Casa",
                            consumoKWh = consumoTotalKWh,
                            tarifaBaseKWh = tarifa.tarifaBaseKWh,
                            adicionalBandeiraKWh = bandeira.adicionalPorKWh,
                            aliquotaICMS = aliquota,
                            precoSemImposto = precoSemImposto,
                            valorICMS = valorImposto,
                            valorTotal = precoSemImposto + valorImposto
                        )
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Consumo estimado",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format(ptBr, "%.1f kWh", consumoTotalKWh),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título da Seção ELETRODOMÉSTICOS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ELETRODOMÉSTICOS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${aparelhos.size} itens",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (aparelhos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum aparelho cadastrado.\nClique no botão '+' para adicionar!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(aparelhos) { aparelho ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val aliquota = tarifa.calcularAliquotaICMS(consumoTotalKWh)
                                    val precoSemImposto = (tarifa.tarifaBaseKWh + bandeira.adicionalPorKWh) * aparelho.consumoMensalKWh
                                    val valorImposto = precoSemImposto * aliquota
                                    detalhesSelecionados = CustoDetalhado(
                                        titulo = aparelho.nome,
                                        consumoKWh = aparelho.consumoMensalKWh,
                                        tarifaBaseKWh = tarifa.tarifaBaseKWh,
                                        adicionalBandeiraKWh = bandeira.adicionalPorKWh,
                                        aliquotaICMS = aliquota,
                                        precoSemImposto = precoSemImposto,
                                        valorICMS = valorImposto,
                                        valorTotal = precoSemImposto + valorImposto
                                    )
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Ícone Temático do Aparelho
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .background(
                                                color = Color(0xFF1E2923),
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = getIconForApparelho(aparelho.icone),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = aparelho.nome,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${aparelho.potenciaWatts.toInt()} W • ${aparelho.horasPorDia.toInt()} h/dia",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = String.format(ptBr, "R$ %.2f", aparelho.calcularCustoMensal(tarifa, bandeira.adicionalPorKWh, consumoTotalKWh)),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = String.format(ptBr, "%.1f kWh", aparelho.consumoMensalKWh),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Função auxiliar para selecionar o ícone salvo
@Composable
private fun getIconForApparelho(nomeIcone: String): ImageVector {
    return when (nomeIcone) {
        "ar" -> Icons.Default.Air
        "tv" -> Icons.Default.Tv
        "geladeira" -> Icons.Default.Kitchen
        "bolt" -> Icons.Default.Bolt
        else -> Icons.Default.Power
    }
}
