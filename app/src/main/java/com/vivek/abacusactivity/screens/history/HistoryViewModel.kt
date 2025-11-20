package com.vivek.abacusactivity.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.vivek.abacusactivity.domain.usecase.GetAllGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllGamesUseCase: GetAllGamesUseCase
) : ViewModel() {

    // Fetch the games and expose them as a StateFlow.
    // The data will be observed by the UI.
    val games = getAllGamesUseCase()
        .stateIn(
            scope = viewModelScope,
            // Start collecting when the UI is visible and stop 5 seconds after it's gone.
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = emptyList() // Start with an empty list.
        )
}