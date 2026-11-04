package com.example.energiainteligente

import java.util.UUID

data class Aparelho(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val potenciaWatts: Double,
    val horasPorDia: Double
) {
    // Conversão exata de Watts para kWh no mês (30 dias)
    val consumoMensalKWh: Double
        get() = (potenciaWatts / 1000.0) * horasPorDia * 30.0

    // Calcula o custo mensal considerando tarifa base + adicional de bandeira + ICMS
    fun calcularCustoMensal(tarifa: TarifaDistribuidora, adicionalBandeira: Double = 0.0): Double {
        val precoSemImposto = (tarifa.tarifaBaseKWh + adicionalBandeira) * consumoMensalKWh
        val aliquotaICMS = tarifa.calcularAliquotaICMS(consumoMensalKWh)
        val valorImposto = precoSemImposto * aliquotaICMS
        return precoSemImposto + valorImposto
    }
}