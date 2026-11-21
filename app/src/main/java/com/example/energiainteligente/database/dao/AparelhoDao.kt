package com.example.energiainteligente.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energiainteligente.database.entity.AparelhoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AparelhoDao {
    @Query("SELECT * FROM aparelho_usuario")
        fun listarAparelhos(): Flow<List<AparelhoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun inserirAparelho(aparelho: AparelhoEntity)

    @Delete
        suspend fun deletarAparelho(aparelho: AparelhoEntity)
}