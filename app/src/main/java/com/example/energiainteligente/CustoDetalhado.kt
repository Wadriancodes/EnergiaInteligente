package com.example.energiainteligente

data class CustoDetalhado(
    val titulo: String,
    val consumoKWh: Double,
    val tarifaBaseKWh: Double,
    val adicionalBandeiraKWh: Double,
    val aliquotaICMS: Double,
    val precoSemImposto: Double,
    val valorICMS: Double,
    val valorTotal: Double
)
