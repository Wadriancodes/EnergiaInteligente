package com.example.energiainteligente

import java.util.UUID

data class Aparelho(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val potenciaWatts: Double,
    val horasPorDia: Double,
    val icone: String = "power"
) {
    // Conversão exata de Watts para kWh no mês (30 dias)
    val consumoMensalKWh: Double
        get() = (potenciaWatts / 1000.0) * horasPorDia * 30.0

    // Calcula o custo mensal considerando a tarifa base + adicional de bandeira + ICMS da conta total
    fun calcularCustoMensal(tarifa: TarifaDistribuidora, adicionalBandeira: Double = 0.0, consumoTotalCasaKWh: Double): Double {
        val precoSemImposto = (tarifa.tarifaBaseKWh + adicionalBandeira) * consumoMensalKWh
        // O ICMS é aplicado com base no consumo total da residência, e não no consumo individual do aparelho
        val aliquotaICMS = tarifa.calcularAliquotaICMS(consumoTotalCasaKWh)
        val valorImposto = precoSemImposto * aliquotaICMS
        return precoSemImposto + valorImposto
    }
}