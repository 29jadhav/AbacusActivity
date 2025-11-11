package com.vivek.abacusactivity.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.domain.usecase.GetAllGamesUseCase

class HistoryViewModelFactory(
    private val gameDao: GameDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            val getAllGamesUseCase = GetAllGamesUseCase(gameDao)
            return HistoryViewModel(getAllGamesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}