package com.example.energiainteligente.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalogo_aparelho")
data class CatalogoAparelhoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_catalogo")
    val idCatalogo: Long = 0,
    
    @ColumnInfo(name = "nome")
    val nome: String,
    
    @ColumnInfo(name = "categoria")
    val categoria: String?,
    
    @ColumnInfo(name = "potencia_w")
    val potenciaWatts: Double,
    
    @ColumnInfo(name = "horas_uso_sugerido")
    val horasUsoSugerido: Double?
)