package com.vivek.abacusactivity.data.repository

import android.util.Log
import com.vivek.abacusactivity.di.DoubleDigitRepo
import com.vivek.abacusactivity.di.SingleDigitRepo
import com.vivek.abacusactivity.domain.model.Problem
import com.vivek.abacusactivity.domain.repository.ProblemRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ProblemRepositoryProvider @Inject constructor(
    @SingleDigitRepo private val singleDigitRepo: ProblemRepository,
    @DoubleDigitRepo private val doubleDigitRepo: ProblemRepository
) : ProblemRepository {

    // Default to single digit
    private var currentRepo: ProblemRepository = singleDigitRepo

    /**
     * Switches the active repository based on the lesson ID.
     * @param lessonId 1 for Single Digit, 2 for Double Digit
     */
    fun setLesson(lessonId: Int) {
        Log.d("TAG1", "setLesson lessonId=$lessonId")
        currentRepo = if (lessonId == 2) {
            doubleDigitRepo
        } else {
            singleDigitRepo
        }
    }

    override fun generateProblem(random: Random): Problem {
        Log.d("TAG1", "generateProblem currentRepo=$currentRepo")
        return currentRepo.generateProblem(random)
    }
}