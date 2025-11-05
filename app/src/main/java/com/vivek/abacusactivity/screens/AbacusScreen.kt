package com.vivek.abacusactivity.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun AbacusScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {
    val uiState by gameViewModel.uiState.collectAsState()
    var userAnswer by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState.gameState) {
            GameState.SETUP -> {
                StartScreen { duration ->
                    gameViewModel.startGame(duration)
                }
            }
            GameState.ACTIVE -> {
                GameActiveScreen(
                    problem = uiState.problem,
                    score = uiState.score,
                    timeRemaining = uiState.timeRemaining,
                    userAnswer = userAnswer,
                    onAnswerChange = { userAnswer = it },
                    onSubmit = {
                        gameViewModel.submitAnswer(userAnswer)
                        userAnswer = "" // Clear input field
                    }
                )
            }
            GameState.FINISHED -> {
                QuizResultScreen(
                    score = uiState.score,
                    results = uiState.results
                ) {
                    gameViewModel.restartGame()
                }
            }
        }
    }
}
