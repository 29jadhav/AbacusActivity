package com.vivek.abacusactivity.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.abacusactivity.domain.usecase.GetGameWithProblemsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ResultViewModel(
    gameId: Long,
    getGameWithProblemsUseCase: GetGameWithProblemsUseCase
) : ViewModel() {

    // Fetch the specific game with its problems and expose it as state
    val gameDetails = getGameWithProblemsUseCase(gameId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = null // Start with null until data is loaded
        )
}