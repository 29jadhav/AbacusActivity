package com.vivek.abacusactivity.domain.usecase

import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.ProblemEntity
import javax.inject.Inject

class SaveProblemResultUseCase @Inject constructor(
    private val gameDao: GameDao
) {
    /**
     * Saves a single problem to the database, associated with a gameId.
     */
    suspend operator fun invoke(problem: ProblemEntity) {
        // The problem object should already contain the gameId
        gameDao.insertProblems(listOf(problem))
    }
}