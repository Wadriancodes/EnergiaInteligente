package com.example.energiainteligente

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class AneelRepository {

    // Configuração do Cliente HTTP apontando para os Dados Abertos
    private val api: AneelApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dadosabertos.aneel.gov.br/") // URL base do CKAN ANEEL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AneelApiService::class.java)
    }

    // Chamada para buscar a Bandeira Vigente no portal CKAN da API
    suspend fun obterBandeiraVigente(): BandeiraTarifaria {
        return try {
            val response = api.obterBandeiraVigente()
            
            if (response.success && response.result.records.isNotEmpty()) {
                val record = response.result.records.first()
                val nomeBandeira = record.nomBandeira.trim().uppercase(Locale.ROOT)
                
                when {
                    nomeBandeira.contains("VERDE") -> BandeiraTarifaria.verde()
                    nomeBandeira.contains("AMARELA") -> BandeiraTarifaria.amarela()
                    nomeBandeira.contains("VERMELHA") && nomeBandeira.contains("PATAMAR 1") -> BandeiraTarifaria.vermelhaP1()
                    nomeBandeira.contains("VERMELHA") && nomeBandeira.contains("PATAMAR 2") -> BandeiraTarifaria.vermelhaP2()
                    // Se for apenas "VERMELHA" sem especificar patamar, assume-se a mais alta por segurança (P2)
                    nomeBandeira.contains("VERMELHA") -> BandeiraTarifaria.vermelhaP2()
                    else -> BandeiraTarifaria.verde() // Fallback padrão
                }
            } else {
                BandeiraTarifaria.verde()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Em caso de falha de rede/offline, fallback seguro para Bandeira Verde
            BandeiraTarifaria.verde()
        }
    }

    // Chamada REST para buscar a Tarifa do Estado (UF) na API
    // TODO: A API de Tarifa também precisa ser mapeada pro formato Dados Abertos (CKAN) futuramente.
    suspend fun obterTarifaPorUF(uf: String = "PA"): TarifaDistribuidora {
        return try {
            val response = api.obterTarifaPorUF(uf)
            TarifaDistribuidora(response.uf, response.distribuidora, response.tarifaBaseKWh)
        } catch (e: Exception) {
            // Fallback para Equatorial Pará (PA)
            TarifaDistribuidora("PA", "Equatorial Pará", 0.97830)
        }
    }
}