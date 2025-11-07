package com.vivek.abacusactivity.screens.start

/**
 * Defines all the events that the user can trigger from the StartScreen UI.
 * This sealed interface is used to ensure type safety and restrict the kinds of
 * events that can be sent to the ViewModel.
 */
sealed class StartScreenEvent {
    // Event for when the user types in the custom time field
    data class OnCustomTimeChange(val time: String) : StartScreenEvent()

    // Event for when the user clicks a predefined time button (e.g., "1 Min")
    data class StartPredefinedTimeGame(val timeInMinutes: Int) : StartScreenEvent()

    // Event for when the user clicks the "Start" button for a custom time
    object StartCustomTimeGame : StartScreenEvent()
}