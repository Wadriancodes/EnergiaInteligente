package com.example.energiainteligente.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "aparelho_usuario")
data class AparelhoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_aparelho")
    val idAparelho: String = UUID.randomUUID().toString(),
    
    // Deixaremos o id_usuario opcional por enquanto, pois ainda não temos a tabela usuário
    @ColumnInfo(name = "id_usuario")
    val idUsuario: String? = null,
    
    @ColumnInfo(name = "id_catalogo")
    val idCatalogo: Long? = null,
    
    @ColumnInfo(name = "nome_personalizado")
    val nomePersonalizado: String,
    
    @ColumnInfo(name = "potencia_w")
    val potenciaWatts: Double,
    
    @ColumnInfo(name = "horas_uso_diario")
    val horasPorDia: Double,
    
    @ColumnInfo(name = "icone")
    val icone: String = "power"
)