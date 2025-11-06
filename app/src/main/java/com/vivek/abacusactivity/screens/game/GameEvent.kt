package com.vivek.abacusactivity.screens.game

/**
 * A sealed class representing all possible user interactions (events)
 * that can occur on the game screens.
 */
sealed class GameEvent {
    /**
     * Event triggered when the user starts the game with a specific duration.
     * @param durationInSeconds The total time for the game.
     */
    data class StartGame(val durationInSeconds: Int) : GameEvent()

    /**
     * Event triggered when the user submits an answer.
     * @param answer The text entered by the user.
     */
    data class SubmitAnswer(val answer: String) : GameEvent()

    /**
     * Event triggered when the user wants to play again after the game is finished.
     */
    data object RestartGame : GameEvent()
    /**
     *  Event triggered when the user wants to pause the game during the play
     * */
    data object PauseGame : GameEvent()
    /**
     *  Event triggered when the user wants to play the game after pausing
     * */
    data object ResumeGame : GameEvent()

}