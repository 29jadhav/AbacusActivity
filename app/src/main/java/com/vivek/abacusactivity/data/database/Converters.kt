package com.vivek.abacusactivity.data.database

import androidx.room.TypeConverter
import com.vivek.abacusactivity.data.entity.GameStatus
import java.util.Date

/**
 * Type converters to allow Room to handle custom types like Date and GameStatus.
 */
class Converters {

    // --- Date to Long Converter ---

    /**
     * Converts a Long from the database into a Date object.
     * Room will store the date as a Long (Unix timestamp).
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Converts a Date object into a Long that can be stored in the database.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // --- GameStatus to String Converter ---

    /**
     * Converts a GameStatus enum into a String for database storage.
     */
    @TypeConverter
    fun fromGameStatus(status: GameStatus): String {
        return status.name
    }

    /**
     * Converts a String from the database back into a GameStatus enum.
     */
    @TypeConverter
    fun toGameStatus(value: String): GameStatus {
        return GameStatus.valueOf(value)
    }
}
