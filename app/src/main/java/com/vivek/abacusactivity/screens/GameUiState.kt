package com.vivek.abacusactivity.screens

data class GameUiState(
    val problem: CalculationProblem = CalculationProblem(emptyList(), 0),
    val score: Int = 0,
    val timeRemaining: Int = 0,
    val gameState: GameState = GameState.SETUP,
    val results: List<ProblemResult> = emptyList()
)