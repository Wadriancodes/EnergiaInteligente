package com.example.energiainteligente.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.energiainteligente.database.entity.CatalogoAparelhoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogoDao {
    @Query("SELECT * FROM catalogo_aparelho ORDER BY nome ASC")
    fun listarCatalogo(): Flow<List<CatalogoAparelhoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirCatalogo(catalogo: List<CatalogoAparelhoEntity>)

}
