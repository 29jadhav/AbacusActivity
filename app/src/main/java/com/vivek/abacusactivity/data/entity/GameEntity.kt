package com.vivek.abacusactivity.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Represents the status of a game saved in the database.
 */
enum class GameStatus {
    COMPLETED,
    TERMINATED,
    IN_PROGRESS
}

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val gameId: Long = 0,
    val timestamp: Date,
    val totalDurationInSeconds: Int, // The total time the game was set for (e.g., 60s)
    val actualDurationPlayedInSeconds: Int, // The actual time the user played (e.g., 35s)
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val status: GameStatus,
    val isSynced: Boolean = false,
    val userId: String? = null // For Firebase synchronization
)