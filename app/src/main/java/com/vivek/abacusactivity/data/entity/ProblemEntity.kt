package com.vivek.abacusactivity.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "problems",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["gameId"],
            childColumns = ["gameOwnerId"],
            onDelete = ForeignKey.CASCADE // If a game is deleted, delete its problems
        )
    ]
)
data class ProblemEntity(
    @PrimaryKey(autoGenerate = true)
    val problemId: Long = 0,
    val gameOwnerId: Long, // Foreign key linking to GameEntity
    val numbers: String, // Storing the list of numbers as a comma-separated String
    val correctAnswer: Int,
    val userAnswer: String,
    val isCorrect: Boolean
)