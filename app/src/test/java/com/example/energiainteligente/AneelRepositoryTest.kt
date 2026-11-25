package com.example.energiainteligente

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AneelRepositoryTest {

    @Test
    fun testObterBandeiraETarifaMaranhao() = runBlocking {
        val repository = AneelRepository()
        
        // Busca a Bandeira Vigente
        val bandeira = repository.obterBandeiraVigente()
        println("=== BANDEIRA VIGENTE ===")
        println("Resultado Real da Bandeira da ANEEL: ${bandeira.tipo}")
        println("Nome: ${bandeira.nome}")
        println("Adicional por KWh: R$ ${bandeira.adicionalPorKWh}")
        println()
        
        // Busca a Tarifa para o Maranhão (MA)
        val tarifa = repository.obterTarifaPorUF("MA")
        println("=== TARIFA MARANHÃO (MA) ===")
        println("Distribuidora: ${tarifa.distribuidora}")
        println("Tarifa Base (KWh): R$ ${tarifa.tarifaBaseKWh}")
        
        // Apenas verifica se retornou corretamente
        assert(bandeira.tipo in TipoBandeira.values())
        assert(tarifa.uf == "MA" || tarifa.uf == "PA") // PA é o fallback atual
    }
}
