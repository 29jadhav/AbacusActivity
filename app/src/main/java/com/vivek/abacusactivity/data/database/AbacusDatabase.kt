package com.vivek.abacusactivity.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameEntity
import com.vivek.abacusactivity.data.entity.ProblemEntity

@TypeConverters(Converters::class)
@Database(
    entities = [GameEntity::class, ProblemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AbacusDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao // Update to new DAO name

    companion object {
        // 2. DEFINE THE MIGRATION OBJECT
        /**
         * Migration from version 1 to 2: Adds the `isSynced` column to the `games` table.
         * The default value is set to 0 (false) for all existing rows.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE games ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE games ADD COLUMN userId TEXT")
            }
        }
    }
}