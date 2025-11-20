package com.vivek.abacusactivity.data.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.data.entity.GameWithProblems
import com.vivek.abacusactivity.data.remote.GameResultDto
import com.vivek.abacusactivity.data.remote.ProblemDto
import com.vivek.abacusactivity.domain.repository.GameRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val gameDao: GameDao // We need the DAO to update sync status
) : GameRepository {

    override suspend fun uploadGameResult(gameWithProblems: GameWithProblems): Result<Unit> {
        val userId = gameWithProblems.game.userId
        if (userId == null) {
            return Result.failure(Exception("Game has no associated user. Cannot upload."))
        }

        return try {
            val gameResultDto = GameResultDto(
                userId = userId,
                totalDurationSeconds = gameWithProblems.game.totalDurationInSeconds,
                actualDurationPlayedSeconds = gameWithProblems.game.actualDurationPlayedInSeconds,
                score = gameWithProblems.problems.count { it.isCorrect },
                status = gameWithProblems.game.status.name,
                timestamp = Timestamp(gameWithProblems.game.timestamp),
                problems = gameWithProblems.problems.map { problem ->
                    ProblemDto(
                        question = problem.numbers,
                        correctAnswer = problem.correctAnswer,
                        userAnswer = problem.userAnswer,
                        isCorrect = problem.isCorrect
                    )
                }
            )

            firestore.collection("game_results")
                .add(gameResultDto)
                .await()

            // After a successful upload, mark the local item as synced.
            gameDao.updateSyncStatus(gameWithProblems.game.gameId, isSynced = true)

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("GameRepositoryImpl", "Failed to upload game result", e)
            Result.failure(e)
        }
    }
}