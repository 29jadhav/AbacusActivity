package com.vivek.abacusactivity.screens.game

import com.vivek.abacusactivity.domain.model.Problem

/**
 * Represents the complete and current state of the Game Screen's UI.
 * This is a temporary, in-memory state.
 */
data class GameUiState(
    /** The current phase of the game (e.g., ACTIVE, PAUSED). */
    val gameState: GameState = GameState.SETUP,

    /** The problem currently displayed to the user. */
    val currentProblem: Problem? = null,

    /** The time remaining in seconds. */
    val timeRemaining: Int = 0,

    /** The user's current score for this session. */
    val score: Int = 0,

    /** Flag to indicate when the timer is low (e.g., <= 10s) to change UI color. */
    val isTimeWarning: Boolean = false,

    /**
     * The ID of the game session once it's finalized.
     * The UI will observe this to know when and where to navigate.
     */
    val finalizedGameId: Long? = null,
    val showTerminateConfirmDialog: Boolean = false
)