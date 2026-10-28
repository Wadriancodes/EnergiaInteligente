package com.example.energiainteligente

data class TarifaDistribuidora(
    val uf: String,
    val distribuidora: String,
    val tarifaBaseKWh: Double,
    val aliquotaICMS: Double = 0.19
) {
    fun calcularPrecoKWhFinal(bandeira: BandeiraTarifaria): Double{
        return tarifaBaseKWh + bandeira.adicionalPorKWh
    }
}