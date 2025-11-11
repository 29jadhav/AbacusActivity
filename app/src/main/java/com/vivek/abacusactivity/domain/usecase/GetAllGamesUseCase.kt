package com.vivek.abacusactivity.domain.usecase

import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameEntity
import kotlinx.coroutines.flow.Flow

/**
 * A use case that retrieves all saved game sessions from the database,
 * ordered from most recent to oldest.
 */
class GetAllGamesUseCase(
    private val gameDao: GameDao
) {
    operator fun invoke(): Flow<List<GameEntity>> {
        return gameDao.getAllGames()
    }
}