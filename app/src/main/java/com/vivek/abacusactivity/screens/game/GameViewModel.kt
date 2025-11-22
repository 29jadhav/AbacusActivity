package com.vivek.abacusactivity.screens.game

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.abacusactivity.data.entity.GameStatus
import com.vivek.abacusactivity.data.entity.ProblemEntity
import com.vivek.abacusactivity.data.repository.ProblemRepositoryProvider
import com.vivek.abacusactivity.domain.repository.AuthRepository
import com.vivek.abacusactivity.domain.repository.ProblemRepository
import com.vivek.abacusactivity.domain.usecase.FinalizeGameUseCase
import com.vivek.abacusactivity.domain.usecase.SaveProblemResultUseCase
import com.vivek.abacusactivity.domain.usecase.StartNewGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val startNewGameUseCase: StartNewGameUseCase,
    private val saveProblemResultUseCase: SaveProblemResultUseCase,
    private val finalizeGameUseCase: FinalizeGameUseCase,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()
    private val durationInSeconds: Int = savedStateHandle.get<Int>("durationInSeconds") ?: 60
    private var currentGameId: Long = -1
    private var timerJob: Job? = null

    init {
        val lessonId = savedStateHandle.get<Int>("lessonId") ?: 1
        Log.d("TAG1","lessonId=====$lessonId")
        if (problemRepository is ProblemRepositoryProvider) {
            problemRepository.setLesson(lessonId)
        }
        startGame()
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
            is GameEvent.SubmitAnswer -> submitAnswer(event.answer)
            GameEvent.PauseGame -> pauseGame()
            GameEvent.ResumeGame -> resumeGame()
            GameEvent.TerminateGame -> onTerminateGame()
            GameEvent.DismissTerminateDialog -> _uiState.update { it.copy(showTerminateConfirmDialog = false) }
            GameEvent.RequestTerminateGame -> _uiState.update { it.copy(showTerminateConfirmDialog = true) }

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

    private fun startGame() {
        if (_uiState.value.gameState != GameState.SETUP) return // Prevent starting multiple times

        viewModelScope.launch {
            val currentUserId = authRepository.currentUser.first()?.uid
            // 1. Create the game in the database and get its ID.
            currentGameId = startNewGameUseCase(durationInSeconds, currentUserId)

            // 2. Update the UI to the active state.
            _uiState.update {
                it.copy(
                    gameState = GameState.ACTIVE,
                    timeRemaining = durationInSeconds,
                    currentProblem = problemRepository.generateProblem(),
                    score = 0,
                    finalizedGameId = null
                )
            }

            // 3. Start the game timer.
            startTimer()
        }
    }

    private fun submitAnswer(answer: String) {
        val currentProblem = _uiState.value.currentProblem ?: return
        if (answer.isBlank() || _uiState.value.gameState != GameState.ACTIVE) return

        val isCorrect = currentProblem.correctAnswer.toString() == answer

        viewModelScope.launch {
            // Create a ProblemEntity to be saved in the database
            val problemEntity = ProblemEntity(
                gameOwnerId = currentGameId,
                numbers = currentProblem.numbers.toString(),
                correctAnswer = currentProblem.correctAnswer,
                userAnswer = answer,
                isCorrect = isCorrect
            )
            saveProblemResultUseCase(problemEntity)

            // Update UI state for the next question and score
            _uiState.update {
                it.copy(
                    currentProblem = problemRepository.generateProblem(),
                    score = if (isCorrect) it.score + 1 else it.score
                )
            }
        }
    }

    private suspend fun finishGame(status: GameStatus) {
        Log.d("TAG1", "finishGame currentGameId=$currentGameId")
        val actualDurationPlayed = durationInSeconds - _uiState.value.timeRemaining
        // Provide the finalized game's ID to the UI to trigger navigation.
        // Finalize the game in the database.
        _uiState.update { it.copy(finalizedGameId = currentGameId, gameState = GameState.FINISHED) }
        timerJob?.cancel()
        finalizeGameUseCase(
            gameId = currentGameId,
            actualDuration = actualDurationPlayed,
            status = status
        )
    }

    fun onTerminateGame() {
        viewModelScope.launch {
            finishGame(GameStatus.TERMINATED)
        }
    }

    private fun handleTimeout() {
        viewModelScope.launch {
            finishGame(GameStatus.COMPLETED)
        }
    }


    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            // This loop now also checks if the game is still active.
            // This prevents the timer from continuing if the game is paused or finished.
            while (_uiState.value.timeRemaining > 0 && _uiState.value.gameState == GameState.ACTIVE) {
                delay(1000L)
                _uiState.update {
                    val newTime = it.timeRemaining - 1
                    it.copy(
                        timeRemaining = newTime,
                        isTimeWarning = newTime in 1..10 // A cleaner way to check a range
                    )
                }
            }

            // FIX: After the loop, check IF the game was still active.
            // This means the loop ended because time ran out, NOT because it was paused.
            if (_uiState.value.gameState == GameState.ACTIVE) {
                // Now, we correctly call handleTimeout to finalize the game.
                handleTimeout()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}