package com.example.energiainteligente

import com.example.energiainteligente.database.dao.AparelhoDao
import com.example.energiainteligente.database.entity.AparelhoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AparelhoRepository(private val aparelhoDao: AparelhoDao) {

    fun listarMeusAparelhos(): Flow<List<Aparelho>> {
        return aparelhoDao.listarAparelhos().map { listaEntities ->
            listaEntities.map { entity ->
                Aparelho(
                    id = entity.idAparelho,
                    nome = entity.nomePersonalizado,
                    potenciaWatts = entity.potenciaWatts,
                    horasPorDia = entity.horasPorDia,
                    icone = entity.icone
                )
            }
        }
    }

    suspend fun salvarAparelho(aparelho: Aparelho) {
        val entity = AparelhoEntity(
            idAparelho = aparelho.id,
            nomePersonalizado = aparelho.nome,
            potenciaWatts = aparelho.potenciaWatts,
            horasPorDia = aparelho.horasPorDia,
            icone = aparelho.icone
        )
        aparelhoDao.inserirAparelho(entity)
    }
}