package com.vivek.abacusactivity.domain.usecase

import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameWithProblems
import kotlinx.coroutines.flow.Flow

/**
 * A use case to retrieve a single game session and all its associated problems.
 */
class GetGameWithProblemsUseCase(
    private val gameDao: GameDao
) {
    operator fun invoke(gameId: Long): Flow<GameWithProblems?> {
        return gameDao.getGameWithProblems(gameId)
    }
}