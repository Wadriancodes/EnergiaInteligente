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

    // Cálculo do ICMS por faixa de consumo (Alíquotas do Pará/ANEEL)
    val aliquotaICMS: Double
        get() = when {
            consumoMensalKWh <= 100.0 -> 0.0     // Isento até 100 kWh
            consumoMensalKWh <= 150.0 -> 0.15    // 15% para 101-150 kWh
            else -> 0.19                         // 19% para > 150 kWh
        }

    // Calcula o custo mensal considerando tarifa base + adicional de bandeira + ICMS
    fun calcularCustoMensal(precoKwhBase: Double, adicionalBandeira: Double = 0.0): Double {
        val precoSemImposto = (precoKwhBase + adicionalBandeira) * consumoMensalKWh
        val valorImposto = precoSemImposto * aliquotaICMS
        return precoSemImposto + valorImposto
    }
}