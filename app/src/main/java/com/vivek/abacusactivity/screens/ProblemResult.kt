package com.vivek.abacusactivity.screens

// Add these new data classes near your CalculationProblem data class
data class ProblemResult(
    val problem: CalculationProblem,
    val userAnswer: String,
    val isCorrect: Boolean
)