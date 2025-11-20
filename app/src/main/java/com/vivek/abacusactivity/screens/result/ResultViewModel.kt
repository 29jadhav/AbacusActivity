package com.vivek.abacusactivity.screens.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.abacusactivity.domain.usecase.GetGameWithProblemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getGameWithProblemsUseCase: GetGameWithProblemsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Retrieve the gameId from the navigation arguments using SavedStateHandle
    private val gameId: Long = savedStateHandle.get<Long>("gameId") ?: -1L

    // Fetch the specific game with its problems and expose it as state
    val gameDetails = getGameWithProblemsUseCase(gameId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = null // Start with null until data is loaded
        )
}