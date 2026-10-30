package com.example.energiainteligente

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AneelRepository {

    // Configuração do Cliente HTTP REST Retrofit
    private val api: AneelApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.aneel.gov.br/") // Endpoint oficial REST
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AneelApiService::class.java)
    }

    // Chamada REST para buscar a Bandeira Vigente na API
    suspend fun obterBandeiraVigente(): BandeiraTarifaria {
        return try {
            val response = api.obterBandeiraVigente()
            // Normaliza a string para evitar problemas com maiúsculas/minúsculas
            when (response.tipo.uppercase()) {
                "AMARELO", "1" -> {
                    BandeiraTarifaria.amarela()
                }
                "VERMELHA_P1", "2" -> {
                    BandeiraTarifaria.vermelhaP1()
                }
                "VERMELHA_P2", "3" -> {
                    BandeiraTarifaria.vermelhaP2()
                }
                "VERDE", "0" -> {
                    BandeiraTarifaria.verde()
                }
                else -> {
                    BandeiraTarifaria.verde() // Fallback padrão
                }
            }
        } catch (e: Exception) {
            // Em caso de falha de rede/offline, fallback seguro para Bandeira Verde
            BandeiraTarifaria.verde()
        }
    }

    // Chamada REST para buscar a Tarifa do Estado (UF) na API
    suspend fun obterTarifaPorUF(uf: String = "PA"): TarifaDistribuidora {
        return try {
            val response = api.obterTarifaPorUF(uf)
            TarifaDistribuidora(response.uf, response.distribuidora, response.tarifaBaseKWh)
        } catch (e: Exception) {
            // Fallback para Equatorial Pará (PA) se a API estiver fora do ar
            TarifaDistribuidora("PA", "Equatorial Pará", 0.97830)
        }
    }
}