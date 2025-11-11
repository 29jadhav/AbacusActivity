package com.vivek.abacusactivity.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameEntity
import com.vivek.abacusactivity.data.entity.ProblemEntity

@Database(
    entities = [GameEntity::class, ProblemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AbacusDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao // Update to new DAO name

    companion object {
        @Volatile
        private var INSTANCE: AbacusDatabase? = null

        fun getDatabase(context: Context): AbacusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AbacusDatabase::class.java,
                    "abacus_database"
                )
                    .fallbackToDestructiveMigration(false) // For development: migrates by destroying old data
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}