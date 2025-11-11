package com.vivek.abacusactivity.domain.usecase

import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameEntity
import com.vivek.abacusactivity.data.entity.ProblemEntity
import com.vivek.abacusactivity.domain.model.ProblemResult

/**
 * This use case encapsulates the logic for saving a completed game session
 * and all of its associated problems to the database.
 */
class SaveGameAndResultsUseCase(
    private val gameDao: GameDao
) {
    suspend operator fun invoke(
        finalScore: Int,
        initialDuration: Int,
        results: List<ProblemResult>
    ): Long {
        // 1. Create the parent GameEntity
        val gameEntity = GameEntity(
            finalScore = finalScore,
            initialDuration = initialDuration
        )

        // 2. Insert the game and get its newly generated ID
        val newGameId = gameDao.insertGame(gameEntity)

        // 3. Create a list of ProblemEntity, linking them to the new game ID
        val problemEntities = results.map { problemResult ->
            ProblemEntity(
                gameOwnerId = newGameId,
                numbers = problemResult.problem.numbers.joinToString(","),
                correctAnswer = problemResult.problem.sum,
                userAnswer = problemResult.userAnswer,
                isCorrect = problemResult.isCorrect
            )
        }

        // 4. Insert all the problems into the database
        gameDao.insertProblems(problemEntities)
        return newGameId
    }
}