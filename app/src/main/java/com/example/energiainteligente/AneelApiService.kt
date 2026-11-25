package com.example.energiainteligente

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// Interface Retrofit para a API de Dados Abertos da ANEEL (CKAN)
interface AneelApiService {

    // Fazemos uma query SQL no dataset da ANEEL para trazer apenas o registro mais recente
    @GET("api/3/action/datastore_search_sql")
    suspend fun obterBandeiraVigente(
        @Query("sql") sql: String = "SELECT * FROM \"b39c0f06-2cdd-41e9-bad5-2ba19e26210f\" ORDER BY \"id\" DESC LIMIT 1"
    ): CkanResponse

    @GET("api/v1/tarifa") // NOTA: A tarifa terá que ser mapeada depois no CKAN também se não existir esse endpoint
    suspend fun obterTarifaPorUF(@Query("uf") uf: String): TarifaResponse
}

// Estrutura complexa de resposta do CKAN
data class CkanResponse(
    val success: Boolean,
    val result: CkanResult
)

data class CkanResult(
    val records: List<BandeiraRecord>
)

data class BandeiraRecord(
    val id: Int,
    @SerializedName("DatGeracaoConjuntoDados") val datGeracao: String,
    @SerializedName("NomBandeira") val nomBandeira: String,
    @SerializedName("VlrAdicionalBandeira") val vlrAdicional: String, // Vem como String, ex: "0,00000"
    @SerializedName("MesReferencia") val mesReferencia: String,
    @SerializedName("AnoReferencia") val anoReferencia: String
)

data class TarifaResponse(
    val uf: String,
    val distribuidora: String,
    val tarifaBaseKWh: Double
)