package com.vivek.abacusactivity.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.domain.repository.GameRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class SyncGamesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val gameDao: GameDao,
    private val gameRepository: GameRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("SyncGamesWorker", "Starting sync job...")

            // 1. Fetch all unsynced finished games
            // You need to add this method to your GameDao:
            // @Query("SELECT * FROM games WHERE isSynced = 0 AND status IN ('COMPLETED', 'TERMINATED')")
            // fun getUnsyncedGames(): List<GameEntity>

            // For now, assuming you fetch the list logic here or via DAO:
            val unsyncedGames = gameDao.getUnsyncedGames()

            if (unsyncedGames.isEmpty()) {
                Log.d("SyncGamesWorker", "No unsynced games found.")
                return@withContext Result.success()
            }

            Log.d("SyncGamesWorker", "Found ${unsyncedGames.size} games to sync.")

            // 2. Iterate and upload each one
            var failureCount = 0

            unsyncedGames.forEach { game ->
                // We need the full object (game + problems) to upload
                val gameWithProblems = gameDao.getGameWithProblems(game.gameId).first()

                if (gameWithProblems != null) {
                    gameRepository.uploadGameResult(gameWithProblems)
                        .onSuccess {
                            Log.i("SyncGamesWorker", "Synced game ${game.gameId}")
                        }
                        .onFailure {
                            Log.e("SyncGamesWorker", "Failed to sync game ${game.gameId}", it)
                            failureCount++
                        }
                }
            }

            if (failureCount > 0) {
                // If some failed, retry later
                Result.retry()
            } else {
                Result.success()
            }

        } catch (e: Exception) {
            Log.e("SyncGamesWorker", "Sync job failed", e)
            Result.retry()
        }
    }
}