package com.vivek.abacusactivity.domain.repository

import com.vivek.abacusactivity.domain.model.Problem
import javax.inject.Inject
import kotlin.random.Random

/**
 * Repository responsible for generating calculation problems.
 * This abstracts the data generation logic from the ViewModel.
 */
class ProblemRepositoryImpl  @Inject constructor() : ProblemRepository {

    fun needsSmallFriend(prev: Int, change: Int): Boolean {
        val newValue = prev + change
        if (newValue !in 0..9) return true // out of abacus range -> treat as invalid

        // bead decomposition
        val prevFives = if (prev >= 5) 1 else 0
        val prevUnits = if (prev >= 5) prev - 5 else prev

        val newFives = if (newValue >= 5) 1 else 0
        val newUnits = if (newValue >= 5) newValue - 5 else newValue

        val fivesDiff = newFives - prevFives
        val unitsDiff = newUnits - prevUnits

        // small-friend needed exactly when the 5-bead toggles and units move in the opposite direction
        return (fivesDiff == 1 && unitsDiff < 0) || // e.g. 3 -> 7 (add a 5, units decrease)
                (fivesDiff == -1 && unitsDiff > 0)    // e.g. 8 -> 3 (remove a 5, units increase)
    }

    fun isValidExample(numbs: List<Int>): Boolean {
        // rules: 4 numbers in -9..9 (no 0), first > 0, cumulative sums 0..9, no small friend at any step
        if (numbs.size != 4) return false
        if (numbs.any { it == 0 }) return false
        if (numbs[0] <= 0) return false

        var total = 0
        for (n in numbs) {
            if (needsSmallFriend(total, n)) return false
            total += n
            if (total !in 0..9) return false
        }
        return true
    }

    override fun generateProblem(random: Random): Problem {
        val vals = (-9..9).filter { it != 0 }
        while (true) {
            val numbs = mutableListOf<Int>()
            numbs += random.nextInt(1, 10)           // first number positive
            repeat(3) { numbs += vals.random(random) } // next three non-zero numbers
            if (isValidExample(numbs)) return Problem(numbs, numbs.sum())
        }
    }
}