package com.vivek.abacusactivity.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivek.abacusactivity.data.dao.GameDao
import com.vivek.abacusactivity.domain.usecase.GetGameWithProblemsUseCase

class ResultViewModelFactory(
    private val gameId: Long,
    private val gameDao: GameDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            val useCase = GetGameWithProblemsUseCase(gameDao)
            return ResultViewModel(gameId, useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}