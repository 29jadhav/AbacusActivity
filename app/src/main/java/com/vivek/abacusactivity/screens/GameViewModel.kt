package com.vivek.abacusactivity.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startGame(durationInSeconds: Int) {
        _uiState.update {
            it.copy(
                timeRemaining = durationInSeconds,
                gameState = GameState.ACTIVE,
                problem = generateAbacusProblem(),
                score = 0,
                results = emptyList()
            )
        }
        startTimer()
    }

    fun submitAnswer(answer: String) {
        val currentState = _uiState.value
        val isCorrect = answer.toIntOrNull() == currentState.problem.sum

        val newResults = currentState.results + ProblemResult(
            problem = currentState.problem,
            userAnswer = answer.ifBlank { "No Answer" },
            isCorrect = isCorrect
        )

        _uiState.update {
            it.copy(
                score = if (isCorrect) it.score + 1 else it.score,
                problem = generateAbacusProblem(),
                results = newResults
            )
        }
    }

    fun restartGame() {
        _uiState.value = GameUiState() // Reset to initial setup state
    }

    private fun startTimer() {
        timerJob?.cancel() // Cancel any existing timer
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeRemaining > 0) {
                delay(1000L)
                _uiState.update { it.copy(timeRemaining = it.timeRemaining - 1) }
            }
            _uiState.update { it.copy(gameState = GameState.FINISHED) }
        }
    }

    private fun generateAbacusProblem(): CalculationProblem {
        while (true) {
            val numbers = mutableListOf<Int>()

            // 1️⃣ First number must be non-negative (1..9)
            numbers.add(Random.nextInt(1, 10))

            var valid = true

            // Generate next 3 numbers with constraints
            while (numbers.size < 4 && valid) {
                val remaining = 4 - numbers.size

                // Try a random next number within [-9, 9]
                val next = generateNonZeroNumber()

                // Check running sum after adding this number
                val newSum = numbers.sum() + next

                if (newSum in 0..9) {
                    numbers.add(next)
                } else {
                    // If invalid, reject and retry
                    valid = false
                }
            }

            if (numbers.size == 4) {
                val total = numbers.sum()

                // Final check: total sum between 1..9
                if (total in 1..9) {
                    return CalculationProblem(numbers, total)
                }
            }
            // If invalid at any point → retry whole sequence
        }
    }

    fun generateNonZeroNumber(): Int {
        var num: Int
        do {
            num = Random.nextInt(-9, 10)
        } while (num == 0)
        return num
    }
}


