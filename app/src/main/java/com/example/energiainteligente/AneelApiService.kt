package com.example.energiainteligente

import retrofit2.http.GET
import retrofit2.http.Query

// Interface Retrofit para as chamadas da API REST ANEEL
interface AneelApiService {

    @GET("api/v1/bandeira")
    suspend fun obterBandeiraVigente(): BandeiraResponse

    @GET("api/v1/tarifa")
    suspend fun obterTarifaPorUF(@Query("uf") uf: String): TarifaResponse
}

data class BandeiraResponse(
    val tipo: String, // "VERDE", "AMARELA", "VERMELHA_P1", "VERMELHA_P2"
    val nome: String,
    val adicionalPorKWh: Double
)

data class TarifaResponse(
    val uf: String,
    val distribuidora: String,
    val tarifaBaseKWh: Double
)