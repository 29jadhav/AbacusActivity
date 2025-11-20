package com.vivek.abacusactivity.data.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

/**
 * Represents the entire game result for a user, to be stored as a document in Firestore.
 */
data class GameResultDto(
    @get:PropertyName("user_id") @set:PropertyName("user_id") var userId: String = "",
    @get:PropertyName("total_duration_seconds") @set:PropertyName("total_duration_seconds") var totalDurationSeconds: Int = 0,
    @get:PropertyName("actual_duration_played_seconds") @set:PropertyName("actual_duration_played_seconds") var actualDurationPlayedSeconds: Int = 0,
    @get:PropertyName("score") @set:PropertyName("score") var score: Int = 0,
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "",
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") var timestamp: Timestamp = Timestamp.now(),
    @get:PropertyName("problems") @set:PropertyName("problems") var problems: List<ProblemDto> = emptyList()
)