package com.vivek.abacusactivity.domain.usecase

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameStatus
import com.vivek.abacusactivity.worker.SyncGamesWorker
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FinalizeGameUseCase @Inject constructor(
    private val gameDao: GameDao,
    private val workManager: WorkManager
) {
    /**
     * Updates a game to a finished status (COMPLETED or TERMINATED)
     * and syncs the final result to Firebase.
     */
    suspend operator fun invoke(
        gameId: Long,
        actualDuration: Int,
        status: GameStatus
    ) {
        // First, get the current game state from the database
        val gameWithProblems = gameDao.getGameWithProblems(gameId).firstOrNull() ?: return
        val game = gameWithProblems.game
        val problems = gameWithProblems.problems

        val correct = problems.count { it.isCorrect }
        val incorrect = problems.size - correct

        val finalizedGame = game.copy(
            actualDurationPlayedInSeconds = actualDuration,
            correctAnswers = correct,
            incorrectAnswers = incorrect,
            status = status
        )


        // Update the local database
        withContext(NonCancellable) {
            gameDao.updateGame(finalizedGame)
        }
        triggerSync()
    }


    private fun triggerSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncGamesWorker>()
            .setConstraints(constraints)
            .build()

        // Use APPEND_OR_REPLACE to ensure this new request runs
        // even if another sync job is currently running.
        workManager.enqueueUniqueWork(
            "SyncGamesImmediate",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            syncRequest
        )
    }
}