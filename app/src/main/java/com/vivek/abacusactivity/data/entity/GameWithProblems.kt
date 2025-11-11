package com.vivek.abacusactivity.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GameWithProblems(
    @Embedded
    val game: GameEntity,

    @Relation(
        parentColumn = "gameId",
        entityColumn = "gameOwnerId"
    )
    val problems: List<ProblemEntity>
)