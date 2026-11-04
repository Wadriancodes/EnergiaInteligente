package com.example.energiainteligente

/*
*
* */
data class TarifaDistribuidora(
    val uf: String,
    val distribuidora: String,
    val tarifaBaseKWh: Double,
    val aliquotaICMS: Double = 0.19
) {
    fun calcularAliquotaICMS(consumoKWh: Double): Double {
        return when (uf.uppercase()) {
            "PA" -> when {
                consumoKWh <= 100.0 -> 0.0
                consumoKWh <= 150.0 -> 0.15
                else -> 0.19
            }
            "SP" -> when {
                consumoKWh <= 90.0 -> 0.0
                consumoKWh <= 200.0 -> 0.12
                else -> 0.18
            }
            "RJ" -> when {
                consumoKWh <= 300.0 -> 0.18
                else -> 0.20
            }
            "SC" -> when {
                consumoKWh <= 150.0 -> 0.12
                else -> 0.17
            }
            "MT" -> when {
                consumoKWh <= 100.0 -> 0.0
                else -> 0.17
            }
            "RS" -> when {
                consumoKWh <= 50.0 -> 0.12
                else -> 0.17
            }

            "PI" -> 0.225
            "AL" -> 0.205
            "BA" -> 0.205
            "PE" -> 0.205
            "AM" -> 0.200
            "CE" -> 0.200
            "DF" -> 0.200
            "MA" -> 0.200
            "PB" -> 0.200
            "RN" -> 0.200
            "RR" -> 0.200
            "SE" -> 0.200
            "TO" -> 0.200
            "RO" -> 0.195
            "AC" -> 0.190
            "GO" -> 0.190
            "PR" -> 0.190
            "AP" -> 0.180
            "MG" -> 0.180
            "ES" -> 0.170
            "MS" -> 0.170

            else -> aliquotaICMS
        }
    }

    fun calcularPrecoKWhFinal(bandeira: BandeiraTarifaria): Double{
        return tarifaBaseKWh + bandeira.adicionalPorKWh
    }
}