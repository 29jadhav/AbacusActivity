package com.vivek.abacusactivity.data.remote

import com.google.firebase.firestore.PropertyName

/**
 * Represents a single problem within a game result stored in Firestore.
 * Using @PropertyName is a robust way to map to Firestore field names.
 */
data class ProblemDto(
    @get:PropertyName("question") @set:PropertyName("question") var question: String = "",
    @get:PropertyName("correct_answer") @set:PropertyName("correct_answer") var correctAnswer: Int = 0,
    @get:PropertyName("user_answer") @set:PropertyName("user_answer") var userAnswer: String = "",
    @get:PropertyName("is_correct") @set:PropertyName("is_correct") var isCorrect: Boolean = false
)