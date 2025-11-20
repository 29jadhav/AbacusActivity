package com.vivek.abacusactivity.screens.game

/**
 * A sealed class representing all possible user interactions (events)
 * that can occur on the game screens.
 */
sealed class GameEvent {
    /**
     * Event triggered when the user submits an answer for the current problem.
     * @param answer The answer provided by the user.
     */
    data class SubmitAnswer(val answer: String) : GameEvent()

    /**
     * Event to pause the currently active game.
     */
    object PauseGame : GameEvent()

    /**
     * Event to resume a paused game.
     */
    object ResumeGame : GameEvent()

    /**
     * Event triggered when the user manually decides to terminate the game.
     */
    object TerminateGame : GameEvent()

    object RequestTerminateGame : GameEvent() // Add this event to ask for confirmation
    object DismissTerminateDialog : GameEvent()
}