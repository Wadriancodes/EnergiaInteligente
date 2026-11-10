package com.example.energiainteligente.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "distribuidora")
data class DistribuidoraEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_distribuidora")
    val idDistribuidora: Long = 0,
    
    @ColumnInfo(name = "nome")
    val nome: String,
    
    @ColumnInfo(name = "uf")
    val uf: String
)