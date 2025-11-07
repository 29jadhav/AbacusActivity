package com.vivek.abacusactivity.screens.start

/**
 * Defines all the events that the user can trigger from the StartScreen UI.
 * This sealed interface is used to ensure type safety and restrict the kinds of
 * events that can be sent to the ViewModel.
 */
sealed interface StartScreenEvent {
    /**
     * This event is triggered whenever the user types into the custom time
     * input field.
     *
     * @param time The new text content of the input field.
     */
    data class OnCustomTimeChange(val time: String) : StartScreenEvent
}