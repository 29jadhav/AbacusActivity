package com.vivek.abacusactivity.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vivek.abacusactivity.domain.repository.ProblemRepository

/**
 * Factory for creating a GameViewModel with a constructor parameter (duration).
 */
class GameViewModelFactory(private val duration: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            // Create and return an instance of GameViewModel, passing the duration.
            // You can also pass the ProblemRepository here if needed.
            return GameViewModel(
                problemRepository = ProblemRepository(),
                durationInSeconds = duration
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}