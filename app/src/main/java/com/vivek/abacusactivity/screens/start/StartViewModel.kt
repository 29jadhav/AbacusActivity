package com.vivek.abacusactivity.screens.start

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// In a new file, e.g., StartViewModel.kt

class StartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StartUiState())
    val uiState = _uiState.asStateFlow()

    fun onCustomTimeChanged(time: String) {
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