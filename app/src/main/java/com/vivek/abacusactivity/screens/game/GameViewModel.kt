package com.vivek.abacusactivity.screens.game

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.abacusactivity.domain.usecase.SaveGameAndResultsUseCase
import com.vivek.abacusactivity.domain.model.ProblemResult
import com.vivek.abacusactivity.domain.repository.ProblemRepository
import com.vivek.abacusactivity.screens.game.GameEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class GameViewModel @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val saveGameAndResults: SaveGameAndResultsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()
    private val durationInSeconds: Int = savedStateHandle.get<Int>("durationInSeconds") ?: 60

    private var timerJob: Job? = null

    init {
        startGame(durationInSeconds)
    }

    override fun onPause(owner: LifecycleOwner) {
        if (_uiState.value.gameState == GameState.ACTIVE) {
            onEvent(GameEvent.PauseGame)
        }
    }

    /**
     * The single entry point for all UI events.
     */
    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.StartGame -> startGame(event.durationInSeconds)
            is GameEvent.SubmitAnswer -> submitAnswer(event.answer)
            GameEvent.PauseGame -> pauseGame()
            GameEvent.ResumeGame -> resumeGame()
            GameEvent.RestartGame -> restartGame()
        }
    }

    private fun saveGameResult(): Job {
        return viewModelScope.launch {
            val finalState = _uiState.value
            // Call the Use Case with the required data
            val newGameId = saveGameAndResults(
                finalScore = finalState.score,
                initialDuration = durationInSeconds,
                results = finalState.results
            )
            _uiState.update { it.copy(lastSavedGameId = newGameId) }
        }
    }

    private fun pauseGame() {
        // Cancel the timer job, but keep the current state
        timerJob?.cancel()
        _uiState.update { it.copy(gameState = GameState.PAUSED) }
    }

    private fun resumeGame() {
        // Simply restart the timer from where it left off
        if (_uiState.value.gameState == GameState.PAUSED) {
            _uiState.update { it.copy(gameState = GameState.ACTIVE) }
            startTimer()
        }
    }

    private fun startGame(durationInSeconds: Int) {
        _uiState.update {
            it.copy(
                timeRemaining = durationInSeconds,
                gameState = GameState.ACTIVE,
                problem = problemRepository.generateNewProblem(),
                score = 0,
                results = emptyList()
            )
        }
        startTimer()
    }

    private fun submitAnswer(answer: String) {
        if (answer.isBlank()) return

        val currentState = _uiState.value
        val isCorrect = answer.toIntOrNull() == currentState.problem.sum

        val newResults = currentState.results + ProblemResult(
            problem = currentState.problem,
            userAnswer = answer,
            isCorrect = isCorrect
        )

        _uiState.update {
            it.copy(
                score = if (isCorrect) it.score + 1 else it.score,
                problem = problemRepository.generateNewProblem(),
                results = newResults
            )
        }
    }

    private fun restartGame() {
        timerJob?.cancel()
        _uiState.value = GameUiState() // Reset to initial setup state
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeRemaining > 0) {
                delay(1000L)
                val newTime = _uiState.value.timeRemaining - 1
                _uiState.update {
                    it.copy(
                        timeRemaining = newTime,
                        // Set flag when time is low
                        isTimeWarning = newTime <= 10
                    )
                }
            }
            saveGameResult().join()
            _uiState.update { it.copy(gameState = GameState.FINISHED) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}