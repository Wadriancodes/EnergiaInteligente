package com.example.energiainteligente

import androidx.compose.ui.graphics.Color

enum class TipoBandeira {
    VERDE,
    AMARELA,
    VERMELHA_P1,
    VERMELHA_P2
}

data class BandeiraTarifaria(
    val tipo: TipoBandeira = TipoBandeira.VERDE,
    val nome: String = "Bandeira Verde",
    val adicionalPorKWh: Double = 0.0,
    val corHex: Color = Color(0xFF4ADE80),
    val descricao: String = "Condições favoráveis de geração de energia"
) {
    companion object {
        fun verde() = BandeiraTarifaria(
            tipo = TipoBandeira.VERDE,
            nome = "Bandeira Verde",
            adicionalPorKWh = 0.0,
            corHex = Color(0xFF4ADE80),
            descricao = "Condições favoráveis de geração de energia"
        )

        fun amarela() = BandeiraTarifaria(
            tipo = TipoBandeira.AMARELA,
            nome = "Bandeira Amarela",
            adicionalPorKWh = 0.01885,
            corHex = Color(0xFFFACC15),
            descricao = "Condições menos favoráveis de geração de energia"
        )

        fun vermelhaP1() = BandeiraTarifaria(
            tipo = TipoBandeira.VERMELHA_P1,
            nome = "Bandeira Vermelha P1",
            adicionalPorKWh = 0.4463,
            corHex = Color(0xFFEF4444),
            descricao = "Condições mais custosas de geração de energia"
        )

        fun vermelhaP2() = BandeiraTarifaria(
            tipo = TipoBandeira.VERMELHA_P2,
            nome = "Bandeira Vermelha P2",
            adicionalPorKWh = 0.07877,
            corHex = Color(0xFFDC2626),
            descricao = "Condições críticas de geração de energia"
        )
    }
}

