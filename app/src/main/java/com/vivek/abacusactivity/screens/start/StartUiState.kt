package com.vivek.abacusactivity.screens.start

data class StartUiState(
    val predefinedTimes: List<Int> = listOf(1, 5, 7, 10),
    val customTimeInput: String = "",
    val isCustomTimeValid: Boolean = false
)