package com.example.energiainteligente

fun calcularCustoMensal(potenciaWatts: Double, horasPorDia: Double, precoKwh: Double): Double {
    val consumoMensalKwh = calcularConsumoMensal(potenciaWatts, horasPorDia)
    return consumoMensalKwh * precoKwh
}

fun calcularConsumoMensal(potenciaWatts: Double, horasPorDia: Double): Double {
    val potenciaKw = potenciaWatts / 1000
    val consumoDiario = potenciaKw * horasPorDia
    return consumoDiario * 30
}