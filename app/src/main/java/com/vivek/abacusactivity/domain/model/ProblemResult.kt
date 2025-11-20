package com.vivek.abacusactivity.domain.model

// Add these new data classes near your CalculationProblem data class
data class ProblemResult(
    val problem: Problem,
    val userAnswer: String,
    val isCorrect: Boolean
)