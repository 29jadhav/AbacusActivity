package com.vivek.abacusactivity.data.repository

import com.vivek.abacusactivity.domain.model.Problem
import com.vivek.abacusactivity.domain.repository.ProblemRepository
import javax.inject.Inject
import kotlin.random.Random

/**
 * Two-digit problem generator based on the reliable single-digit algorithm.
 *
 * Rules:
 * 1) Each two-digit number is split into TENS and UNITS.
 * 2) Both TENS and UNITS digits follow the **single-digit abacus rule**
 *    except ONE modification:
 *        → If UNITS digit is ZERO but TENS digit is NON-ZERO, we DO NOT reject.
 *          (Because units do not move on the abacus when they are zero)
 * 3) At every step the cumulative tens and units values must stay in 0..9
 *      → No negative
 *      → No carry
 * 4) At no step should small-friend be required for either digit.
 */
class DoubleDigitProblemRepositoryImpl @Inject constructor() : ProblemRepository {

    // ---------------------------------------------------------
    //   SINGLE-DIGIT SMALL FRIEND CHECK (your original logic)
    // ---------------------------------------------------------
    fun needsSmallFriend(prev: Int, change: Int): Boolean {
        val newValue = prev + change
        if (newValue !in 0..9) return true

        val prevFives = if (prev >= 5) 1 else 0
        val prevUnits = if (prev >= 5) prev - 5 else prev

        val newFives = if (newValue >= 5) 1 else 0
        val newUnits = if (newValue >= 5) newValue - 5 else newValue

        val fivesDiff = newFives - prevFives
        val unitsDiff = newUnits - prevUnits

        return (fivesDiff == 1 && unitsDiff < 0) ||       // e.g 3 → 7
                (fivesDiff == -1 && unitsDiff > 0)         // e.g 8 → 3
    }


    // ---------------------------------------------------------
    //   TWO-DIGIT VALIDATION USING SINGLE-DIGIT RULES
    // ---------------------------------------------------------
    fun isValidTwoDigitProblem(nums: List<Int>): Boolean {
        if (nums.size != 4) return false
        if (nums[0] <= 0) return false
        if (nums.any { it == 0 }) return false

        var tensTotal = 0
        var unitsTotal = 0

        for (num in nums) {

            val tens = num / 10        // Tens digit change
            val units = num % 10       // Units digit change

            // ---------- TENS Digit Check ----------
            // Prevent negative or >9 before applying rule
            if (tensTotal + tens !in 0..9) return false

            // Small-friend rule for tens digit
            if (needsSmallFriend(tensTotal, tens)) return false

            // Apply
            tensTotal += tens


            // ---------- UNITS Digit Check ----------
            // Prevent negative or >9
            if (unitsTotal + units !in 0..9) return false

            // Special exception:
            // If units == 0 AND tens != 0 → allow (do not check small-friend)
            if (!(units == 0 && tens != 0)) {
                if (needsSmallFriend(unitsTotal, units)) return false
            }

            // Apply
            unitsTotal += units
        }

        return true
    }


    // ---------------------------------------------------------
    //   PROBLEM GENERATOR
    // ---------------------------------------------------------
    override fun generateProblem(random: Random): Problem {
        val allowed = (-99..99).filter { it != 0 }

        while (true) {
            val nums = mutableListOf<Int>()

            // First number must be positive (1..99)
            nums += random.nextInt(1, 100)

            // Next three can be ±1..±99 (non-zero)
            repeat(3) { nums += allowed.random(random) }

            if (isValidTwoDigitProblem(nums)) {
                return Problem(nums, nums.sum())
            }
        }
    }
}
