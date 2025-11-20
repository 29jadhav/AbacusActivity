package com.vivek.abacusactivity.domain.usecase

import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameEntity
import com.vivek.abacusactivity.data.entity.GameStatus
import java.util.Date
import javax.inject.Inject

class StartNewGameUseCase @Inject constructor(
    private val gameDao: GameDao,
) {
    /**
     * Creates a new game record in the database with IN_PROGRESS status
     * and returns its new ID.
     */
    suspend operator fun invoke(totalDuration: Int, userId: String? = null): Long {
        val newGame = GameEntity(
            timestamp = Date(),
            totalDurationInSeconds = totalDuration,
            actualDurationPlayedInSeconds = 0,
            correctAnswers = 0,
            incorrectAnswers = 0,
            status = GameStatus.IN_PROGRESS,
            userId = userId,
            isSynced = false
        )
        return gameDao.insertGame(newGame)
    }
}