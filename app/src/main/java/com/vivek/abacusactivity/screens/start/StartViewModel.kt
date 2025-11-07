package com.vivek.abacusactivity.screens.start

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StartUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Handles all user actions from the StartScreen.
     * This is the single entry point for events from the UI.
     */
    fun onEvent(event: StartScreenEvent) {
        when (event) {
            // 1. This 'when' branch now matches the event being sent from the UI
            is StartScreenEvent.OnCustomTimeChange -> {
                handleCustomTimeChange(event.time)
            }
        }
    }

    // 2. The logic is moved into a private function for better organization
    private fun handleCustomTimeChange(time: String) {
        // Only process the update if the input contains only digits
        if (time.all { it.isDigit() }) {
            val timeAsInt = time.toIntOrNull()
            _uiState.update {
                it.copy(
                    customTimeInput = time,
                    isCustomTimeValid = timeAsInt != null && timeAsInt > 0
                )
            }
        }
    }
}
