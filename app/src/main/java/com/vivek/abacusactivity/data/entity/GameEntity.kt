package com.vivek.abacusactivity.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val gameId: Long = 0, // Use Long for IDs that could grow large
    val finalScore: Int,
    val initialDuration: Int,
    val timestamp: Long = System.currentTimeMillis()
)