package com.vivek.abacusactivity.screens.start

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.abacusactivity.utils.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StartUiState())
    val uiState = _uiState.asStateFlow()

    // A SharedFlow is used to send one-time events from the ViewModel to the UI, like navigation.
    private val _navigationEvent = MutableSharedFlow<Int>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /**
     * Handles all user actions from the StartScreen.
     * This is the single entry point for events from the UI.
     */
    fun onEvent(event: StartScreenEvent) {
        when (event) {
            is StartScreenEvent.OnCustomTimeChange -> {
                val input = event.time
                // Allow only digits and ensure it's a reasonable number
                if (input.all { it.isDigit() } && input.length <= 3) {
                    _uiState.update {
                        it.copy(
                            customTimeInput = input,
                            isCustomTimeValid = input.isNotBlank() && input.toInt() > 0
                        )
                    }
                }
            }

            is StartScreenEvent.StartPredefinedTimeGame -> {
                val timeInSeconds = event.timeInMinutes * Constants.SECONDS_IN_MINUTE
                // Launch a coroutine to send the navigation event
                viewModelScope.launch {
                    _navigationEvent.emit(timeInSeconds)
                }
            }

            is StartScreenEvent.StartCustomTimeGame -> {
                val timeInMinutes =
                    _uiState.value.customTimeInput.toIntOrNull() ?: Constants.DEFAULT_TIME_VALUE
                if (timeInMinutes > 0) {
                    val timeInSeconds = timeInMinutes * Constants.SECONDS_IN_MINUTE
                    viewModelScope.launch {
                        _navigationEvent.emit(timeInSeconds)
                    }
                }
            }
        }
    }
}
