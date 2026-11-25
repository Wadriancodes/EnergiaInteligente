package com.example.energiainteligente.network

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class LocationService(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    @SuppressLint("MissingPermission")
    suspend fun obterUfDoUsuario(): String? = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    try {
                        val geocoder = Geocoder(context, Locale("pt", "BR"))
                        // Usamos maxResults = 1 para pegar o endereço mais próximo
                        val enderecos = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (!enderecos.isNullOrEmpty()) {
                            // adminArea retorna o Estado (ex: São Paulo, Pará)
                            val estadoPorExtenso = enderecos[0].adminArea
                            val uf = converterEstadoParaUf(estadoPorExtenso)
                            continuation.resume(uf)
                        } else {
                            continuation.resume(null)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        continuation.resume(null)
                    }
                } else {
                    continuation.resume(null)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                continuation.resume(null)
            }
    }

    // Função auxiliar simples para transformar "São Paulo" em "SP"
    private fun converterEstadoParaUf(nomeEstado: String?): String? {
        if (nomeEstado == null) return null

        val mapaUfs = mapOf(
            "Acre" to "AC", "Alagoas" to "AL", "Amapá" to "AP", "Amazonas" to "AM",
            "Bahia" to "BA", "Ceará" to "CE", "Distrito Federal" to "DF", "Espírito Santo" to "ES",
            "Goiás" to "GO", "Maranhão" to "MA", "Mato Grosso" to "MT", "Mato Grosso do Sul" to "MS",
            "Minas Gerais" to "MG", "Pará" to "PA", "Paraíba" to "PB", "Paraná" to "PR",
            "Pernambuco" to "PE", "Piauí" to "PI", "Rio de Janeiro" to "RJ", "Rio Grande do Norte" to "RN",
            "Rio Grande do Sul" to "RS", "Rondônia" to "RO", "Roraima" to "RR", "Santa Catarina" to "SC",
            "São Paulo" to "SP", "Sergipe" to "SE", "Tocantins" to "TO"
        )
        return mapaUfs[nomeEstado]
    }
}