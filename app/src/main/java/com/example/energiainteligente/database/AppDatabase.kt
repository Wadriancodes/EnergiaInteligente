package com.example.energiainteligente.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.energiainteligente.database.dao.AparelhoDao
import com.example.energiainteligente.database.entity.AparelhoEntity
import com.example.energiainteligente.database.entity.DistribuidoraEntity

@Database(
    entities = [AparelhoEntity::class, DistribuidoraEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aparelhoDao(): AparelhoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "energia_inteligente_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}