package com.vivek.abacusactivity.screens.game

import com.vivek.abacusactivity.domain.model.CalculationProblem
import com.vivek.abacusactivity.domain.model.ProblemResult

data class GameUiState(
    val problem: CalculationProblem = CalculationProblem(emptyList(), 0),
    val score: Int = 0,
    val timeRemaining: Int = 0,
    val isTimeWarning: Boolean = false,
    val gameState: GameState = GameState.SETUP,
    val results: List<ProblemResult> = emptyList()
)