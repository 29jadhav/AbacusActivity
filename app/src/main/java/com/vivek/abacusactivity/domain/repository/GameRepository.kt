package com.vivek.abacusactivity.domain.repository

import com.vivek.abacusactivity.data.entity.GameWithProblems

interface GameRepository {
    /**
     * Uploads a completed game session to the remote data source (Firestore).
     * @param gameWithProblems The local game entity including its list of problems.
     * @return A Result indicating success or failure.
     */
    suspend fun uploadGameResult(gameWithProblems: GameWithProblems): Result<Unit>

    // In the future, you would add more methods here, like:
    // fun getGameHistory(userId: String): Flow<List<GameResultDto>>
}