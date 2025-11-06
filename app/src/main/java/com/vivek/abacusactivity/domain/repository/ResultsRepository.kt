package com.vivek.abacusactivity.domain.repository

import com.vivek.abacusactivity.domain.model.CalculationProblem
import com.vivek.abacusactivity.domain.model.ProblemResult


/**
 * This is a simple singleton object to temporarily hold the results data
 * during navigation between the Game and Result screens.
 */
object ResultsRepository {

    // 2. DEFINE THE VARIABLE with the correct type: List<ProblemResult>
    private var currentResults: List<ProblemResult>? = null

    /**
     * Stores the list of results. Call this right before navigating
     * from the game screen.
     * @param results The List<ProblemResult> from the finished game.
     */
    // 3. The 'holdResults' function MUST ACCEPT this type.
    fun holdResults(results: List<ProblemResult>) {
        currentResults = results
    }

    /**
     * Retrieves the list of results and clears the cache. Call this
     * from the result screen.
     * @return The stored List<ProblemResult>, or an empty list if none exists.
     */
    // 4. The 'retrieveResults' function MUST RETURN this type.
    fun retrieveResults(): List<ProblemResult> {
        // Retrieve the stored results into a temporary variable
        val resultsToReturn = currentResults

        // Immediately clear the repository to prevent memory leaks or stale data
        currentResults = null

        // Return the retrieved list, or an empty list if it was already null
        return resultsToReturn ?: emptyList()
    }
}